import { useState, useEffect } from "react";
import PropertyForm from "../property/PropertyForm";
import CustomerForm from "../customer/CustomerForm";
import SearchProperty  from "../property/SearchProperty"

export default function EmployeeDashboard() {
  const [activePage, setActivePage] = useState("companyInfo");
  const [company, setCompany] = useState(null);
  const [properties, setProperties] = useState([]);
  const [customers, setCustomers] = useState([]);

  const menuItems = [
    { key: "companyInfo", label: "Company Info" },
    { key: "addProperty", label: "İlan Ekle" },
    { key: "addCustomer", label: "Müşteri Ekle" },
    { key: "SearchProperty", label: "İlan Ara"},
    { key: "propertyList", label: "İlan Listesi" },
    { key: "customerList", label: "Müşteri Listesi" },
  ];

  useEffect(() => {
    const fetchJson = async (url) => {
      try {
        const res = await fetch(url);
        const text = await res.text();
        if (!res.ok) return null;
        return JSON.parse(text);
      } catch {
        return null;
      }
    };

    (async () => {
      const c = await fetchJson("http://localhost:8080/api/companies/latest");
      setCompany(c);

      const pr = await fetchJson("http://localhost:8080/api/properties");
      setProperties(Array.isArray(pr) ? pr : pr?.data ?? pr?.content ?? []);

      const cu = await fetchJson("http://localhost:8080/api/customers");
      setCustomers(Array.isArray(cu) ? cu : cu?.data ?? cu?.content ?? []);
    })();
  }, []);

  return (
    <div style={{ display: "flex", height: "100vh", background: "#f0f0f0" }}>
      {/* Sidebar */}
      <aside style={{ width: 250, background: "#fff", padding: 20 }}>
        <h3>Employee Dashboard</h3>
        {menuItems.map((m) => (
          <div
            key={m.key}
            style={{
              padding: "8px 10px",
              cursor: "pointer",
              background: activePage === m.key ? "#eee" : undefined,
              marginBottom: 6,
            }}
            onClick={() => setActivePage(m.key)}
          >
            {m.label}
          </div>
        ))}
      </aside>

      {/* Main Content */}
      <main style={{ flex: 1, padding: 20, overflowY: "auto" }}>
        {activePage === "companyInfo" && (
          <section>
            <h2>Company Info</h2>
            {company ? (
              <div>
                <p><strong>Ad:</strong> {company.name}</p>
                <p><strong>Adres:</strong> {company.address}</p>
                <p><strong>Telefon:</strong> {company.phone}</p>
                <p><strong>Email:</strong> {company.email}</p>
              </div>
            ) : (
              <p>Company bilgisi yükleniyor...</p>
            )}
          </section>
        )}

        {activePage === "addProperty" && (
          <section>
            <h2>İlan Ekle</h2>
            <PropertyForm />
          </section>
        )}

        {activePage === "addCustomer" && (
          <section>
            <h2>Müşteri Ekle</h2>
            <CustomerForm />
          </section>
        )}

        {activePage === "SearchProperty" && (
        <section>
            <h2>İlan Arama</h2>
            <SearchProperty />
        </section>
        )}

        {activePage === "propertyList" && (
          <section>
            <h2>İlan Listesi</h2>
            {properties.length > 0 ? (
              <ul>
                {properties.map((prop) => (
                  <li key={prop.id ?? JSON.stringify(prop)}>
                    <strong>Başlık:</strong> {prop.title || prop.name || "Başlık yok"} <br/>
                    <strong>Tip:</strong> {prop.propertyType || prop.category || "Tip yok"} <br/>
                    <strong>Fiyat:</strong> {prop.price ?? "Belirtilmemiş"} <br/>
                    <strong>Açıklama:</strong> {prop.description ?? prop.aciklama ?? "Yok"}
                  </li>
                ))}
              </ul>
            ) : (
              <p>Henüz ilan yok.</p>
            )}
          </section>
        )}

        {activePage === "customerList" && (
          <section>
            <h2>Müşteri Listesi</h2>
            {customers.length > 0 ? (
              <ul>
                {customers.map((cust) => (
                  <li key={cust.id ?? JSON.stringify(cust)}>
                    <strong>Ad Soyad:</strong>{" "}
                    {cust.firstName && cust.lastName ? `${cust.firstName} ${cust.lastName}` : "İsim yok"} <br/>
                    <strong>Telefon:</strong> {cust.phone || cust.telefon || cust.gsm || "Telefon yok"} <br/>
                    <strong>Müşteri Tipi:</strong> {cust.customerType ?? "Belirtilmemiş"} <br/>
                  </li>
                ))}
              </ul>
            ) : (
              <p>Henüz müşteri yok.</p>
            )}
          </section>
        )}
      </main>
    </div>
  );
}
