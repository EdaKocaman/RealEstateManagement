import { useState, useEffect } from "react";
import EmployeeForm from "../employee/EmployeeForm";
import { useNavigate } from "react-router-dom";

 

export default function CompanyDashboard() {
    const navigate = useNavigate();
    const [activePage, setActivePage] = useState("companyInfo");
    const [company, setCompany] = useState(null);
    const [employees, setEmployees] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [properties, setProperties] = useState([]);

    const [loginPhone, setLoginPhone] = useState("");
    const [loginName, setLoginName] = useState("");

    const menuItems = [
        { key: "companyInfo", label: "İşyeri Bilgisi" },
        { key: "addEmployee", label: "Çalışan Ekle" },
        { key: "employeeList", label: "Çalışan Listesi" },
        { key: "employeeLogin", label: "Çalışan Giriş" },
        { key: "propertyList", label: "İlan Listesi" },
        { key: "customerList", label: "Müşteri Listesi" },
    ];

    useEffect(() => {
        const fetchJson = async (url) => {
        try {
            const res = await fetch(url);
            const text = await res.text();
            if (!res.ok) {
            console.error(`[fetch error] ${url} status=${res.status}`, text);
            return null;
            }
            try {
            return JSON.parse(text);
            } catch (err) {
            console.error(`[json parse error] ${url}`, err, text);
            return null;
            }
        } catch (err) {
            console.error(`[network error] ${url}`, err);
            return null;
        }
        };

        const normalizeEmployees = (raw) => {
        if (!Array.isArray(raw)) {
            // backend farklı bir sarma kullanmış olabilir (e.g. { data: [...]} veya { content: [...] })
            if (raw?.data && Array.isArray(raw.data)) raw = raw.data;
            else if (raw?.content && Array.isArray(raw.content)) raw = raw.content;
            else if (raw == null) raw = [];
            else {
            console.warn("employees: beklenmeyen format, raw:", raw);
            raw = [];
            }
        }

        console.log("rawEmployees sample:", raw.slice(0, 5));
        // Normalize: try name, firstName+lastName, fullName, isim+soyisim
        const normalized = raw.map((emp, i) => {
            if (!emp || typeof emp !== "object") {
            console.warn("employee item not object at index", i, emp);
            emp = {};
            }
            const maybeName =
            emp.name ||
            (emp.firstName && emp.lastName && `${emp.firstName} ${emp.lastName}`) ||
            emp.fullName ||
            (emp.isim && emp.soyisim && `${emp.isim} ${emp.soyisim}`) ||
            emp.isim ||
            emp.firstname ||
            "";

            const maybePhone =
            emp.phone ||
            emp.telefon ||
            emp.tel ||
            emp.phoneNumber ||
            emp.gsm ||
            "";

            const name = String(maybeName || "").trim();
            const phone = String(maybePhone || "").replace(/\D/g, ""); // only digits

            return {
            ...emp,
            _normalizedName: name,
            _normalizedPhone: phone,
            };
        });

        console.log("normalizedEmployees sample:", normalized.slice(0, 5));
        return normalized;
        };

        (async () => {
        const c = await fetchJson("http://localhost:8080/api/companies/latest");
        setCompany(c);
        const em = await fetchJson("http://localhost:8080/api/employees");
        setEmployees(normalizeEmployees(em || []));
        const pr = await fetchJson("http://localhost:8080/api/properties");
        setProperties(Array.isArray(pr) ? pr : pr?.data ?? pr?.content ?? []);
        const cu = await fetchJson("http://localhost:8080/api/customers");
        setCustomers(Array.isArray(cu) ? cu : cu?.data ?? cu?.content ?? []);
        })();
    }, []);

    const handleLogin = (e) => {
        e.preventDefault();

        const normalizedPhone = String(loginPhone || "").replace(/\D/g, "");
        const normalizedName = String(loginName || "").trim().toLowerCase();

        if (!normalizedPhone) {
        alert("Lütfen telefon girin.");
        return;
        }
        if (!normalizedName) {
        alert("Lütfen isim soyisim girin.");
        return;
        }

        // employees stored with _normalizedPhone/_normalizedName
        const matched = employees.find(
        (emp) =>
            emp._normalizedPhone === normalizedPhone &&
            String(emp._normalizedName).toLowerCase() === normalizedName
        );

        if (matched) {
        console.log("Matched employee:", matched);
        alert(`Giriş başarılı! Hoşgeldiniz, ${matched._normalizedName}`);
        navigate("/EmployeeDashboard");
        return;
        }

        // detaylı hata tespiti
        const phoneExists = employees.some((emp) => emp._normalizedPhone === normalizedPhone);
        const nameExists = employees.some(
        (emp) => String(emp._normalizedName).toLowerCase() === normalizedName
        );

        if (!phoneExists && !nameExists) {
        alert("Ne telefon ne de isim/soyisim eşleşmiyor.");
        } else if (!phoneExists) {
        // show similar names for given phone or similar phones for given name
        const sameName = employees.filter(
            (emp) => String(emp._normalizedName).toLowerCase() === normalizedName
        );
        console.warn("sameName matches:", sameName);
        alert("Telefon numarası kayıtlı değil. Girilen isim sistemde mevcut ama telefon farklı.");
        } else if (!nameExists) {
        const samePhone = employees.filter((emp) => emp._normalizedPhone === normalizedPhone);
        console.warn("samePhone matches:", samePhone);
        alert("İsim/soyisim kayıtlı değil veya telefon numarasına ait isim farklı.");
        } else {
        alert("Eşleşme sağlanamadı.");
        }

        // debug output
        console.log("login attempt:", { normalizedPhone, normalizedName });
        console.log("employees sample:", employees.slice(0, 8));
    };

    // basit UI (kısaltılmış)
    return (
        <div style={{ display: "flex", height: "100vh", background: "#f0f0f0" }}>
        <aside style={{ width: 250, background: "#fff", padding: 20 }}>
            <h3>Dashboard</h3>
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

        <main style={{ flex: 1, padding: 20, overflowY: "auto" }}>
            {activePage === "companyInfo" && (
                <section>
                <h2>İşyeri Bilgisi</h2>
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

            {activePage === "employeeLogin" && (
            <section>
                <h2>Çalışan Giriş</h2>
                <form onSubmit={handleLogin} style={{ maxWidth: 360 }}>
                <input
                    placeholder="Telefon (örn: 05551234567)"
                    value={loginPhone}
                    onChange={(e) => setLoginPhone(e.target.value)}
                    style={{ width: "100%", padding: 8, marginBottom: 8 }}
                />
                <input
                    placeholder="İsim Soyisim"
                    value={loginName}
                    onChange={(e) => setLoginName(e.target.value)}
                    style={{ width: "100%", padding: 8, marginBottom: 8 }}
                />
                <button type="submit" style={{ padding: "8px 12px" }}>
                    Giriş
                </button>
                </form>
            </section>
            )}

            {activePage === "addEmployee" && (
                <section>
                <h2>Çalışan Ekle</h2>
                <EmployeeForm />
                </section>
            )}


            {/* Diğer bölümler (kısa örnek) */}
            {activePage === "employeeList" && (
            <section>
                <h2>Çalışan Listesi</h2>
                <ul>
                {employees.map((emp) => (
                    <li key={emp.id ?? JSON.stringify(emp)}>
                    {emp._normalizedName} — {emp._normalizedPhone} — {emp.email ?? emp.eposta ?? ""}
                    </li>
                ))}
                </ul>
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
                        <strong>Ad Soyad:</strong> {cust.firstName && cust.lastName ? `${cust.firstName} ${cust.lastName}` : "İsim yok"} <br/>
                        <strong>Telefon:</strong> {cust.phone || cust.telefon || cust.gsm || "Telefon yok"} <br/>
                        <strong>Müsteri Tippi:</strong> {cust.customerType} <br/>
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
