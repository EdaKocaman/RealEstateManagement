import { useState, useEffect } from "react";

const PROPERTIES_URL = "http://localhost:8080/api/properties";
const CUSTOMERS_URL = "http://localhost:8080/api/customers";
const EMPLOYEES_URL = "http://localhost:8080/api/employees";

export default function PropertyForm() {
  const [formData, setFormData] = useState({
    title: "",
    address: "",
    description: "",
    propertyType: "",
    squareMeters: "",
    roomCount: "",
    floorNumber: "",
    heatingType: "",
    status: "",
    price: "",
    customerId: "",
    employeeId: ""
  });

  const [customers, setCustomers] = useState([]);
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState({ customers: true, employees: true });
  const [errors, setErrors] = useState({ customers: null, employees: null });
  const [submitting, setSubmitting] = useState(false);

  const roomOptions = ["1+0", "1+1", "2+1", "3+1", "4+1", "5+1"];
  const propertyStatusOptions = ["FOR_SALE", "FOR_RENT", "SOLD", "RENTED"];

  useEffect(() => {
    const fetchCustomers = async () => {
      try {
        setLoading(prev => ({ ...prev, customers: true }));
        const res = await fetch(CUSTOMERS_URL);
        if (!res.ok) throw new Error("Müşteriler yüklenemedi");
        const data = await res.json();
        setCustomers(data); // data => [{customerId: "uuid", firstName, lastName}, ...]
      } catch (err) {
        setErrors(prev => ({ ...prev, customers: err.message }));
      } finally {
        setLoading(prev => ({ ...prev, customers: false }));
      }
    };

    const fetchEmployees = async () => {
      try {
        setLoading(prev => ({ ...prev, employees: true }));
        const res = await fetch(EMPLOYEES_URL);
        if (!res.ok) throw new Error("Çalışanlar yüklenemedi");
        const data = await res.json();
        setEmployees(data); // data => [{employeeId: "uuid", firstName, lastName}, ...]
      } catch (err) {
        setErrors(prev => ({ ...prev, employees: err.message }));
      } finally {
        setLoading(prev => ({ ...prev, employees: false }));
      }
    };

    fetchCustomers();
    fetchEmployees();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.customerId || !formData.employeeId) {
      alert("Lütfen müşteri ve çalışan seçiniz.");
      return;
    }

    try {
      setSubmitting(true);
      const payload = {
        ...formData,
        squareMeters: parseInt(formData.squareMeters, 10),
        floorNumber: parseInt(formData.floorNumber, 10),
        price: parseFloat(formData.price)
      };

      const res = await fetch(PROPERTIES_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        let msg = "Kayıt başarısız";
        try {
          const errBody = await res.json();
          msg = errBody.message || msg;
        } catch {}
        throw new Error(msg);
      }

      const saved = await res.json();
      console.log("Property saved:", saved);
      alert("Emlak kaydı başarılı");

      // form reset
      setFormData({
        title: "",
        address: "",
        description: "",
        propertyType: "",
        squareMeters: "",
        roomCount: "",
        floorNumber: "",
        heatingType: "",
        status: "",
        price: "",
        customerId: "",
        employeeId: ""
      });

    } catch (err) {
      console.error(err);
      alert(`Kaydetme başarısız: ${err.message}`);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div style={{ maxWidth: 500, margin: "0 auto", padding: 20, background: "#fff", borderRadius: 8, boxShadow: "0 2px 6px rgba(0,0,0,0.1)" }}>
      <h2 style={{ marginBottom: 20, textAlign: "center" }}>Yeni Emlak Kaydı</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: 12 }}>
          <label>Başlık *</label>
          <input type="text" name="title" value={formData.title} onChange={handleChange} required style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Adres *</label>
          <input type="text" name="address" value={formData.address} onChange={handleChange} required style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Emlak Tipi *</label>
          <input type="text" name="propertyType" value={formData.propertyType} onChange={handleChange} required style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Metrekare *</label>
          <input type="number" name="squareMeters" value={formData.squareMeters} onChange={handleChange} required style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Oda Sayısı *</label>
          <select name="roomCount" value={formData.roomCount} onChange={handleChange} required style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }}>
            <option value="">Select Room Count</option>
            {roomOptions.map(r => <option key={r} value={r}>{r}</option>)}
          </select>
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Bulunduğu Kat *</label>
          <input type="number" name="floorNumber" value={formData.floorNumber} onChange={handleChange} required style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Isınma Tipi</label>
          <input type="text" name="heatingType" value={formData.heatingType} onChange={handleChange} style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Satış Tipi *</label>
          <select name="status" value={formData.status} onChange={handleChange} required style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }}>
            <option value="">Select Status</option>
            {propertyStatusOptions.map(s => <option key={s} value={s}>{s}</option>)}
          </select>
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Ücret *</label>
          <input type="number" name="price" value={formData.price} onChange={handleChange} required style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
        </div>

        {/* Customer */}
        <div style={{ marginBottom: 12 }}>
          <label>Müşteri *</label>
          {loading.customers ? (
            <p>Loading customers...</p>
          ) : errors.customers ? (
            <p style={{ color: "red" }}>{errors.customers}</p>
          ) : (
            <select name="customerId" value={formData.customerId} onChange={handleChange} required style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }}>
              <option value="">Select Customer</option>
              {customers.map(c => (
                <option key={c.id} value={c.id}>
                {c.firstName} {c.lastName}
              </option>
              ))}
            </select>
          )}
        </div>

        {/* Employee */}
        <div style={{ marginBottom: 12 }}>
          <label>Çalışan *</label>
          {loading.employees ? (
            <p>Loading employees...</p>
          ) : errors.employees ? (
            <p style={{ color: "red" }}>{errors.employees}</p>
          ) : (
            <select name="employeeId" value={formData.employeeId} onChange={handleChange} required style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }}>
              <option value="">Select Employee</option>
              {employees.map(e => (
                <option key={e.id} value={e.id}>
                {e.firstName} {e.lastName}
              </option>
              ))}
            </select>
          )}
        </div>

        <div style={{ marginBottom: 12 }}>
          <label>Açıklama</label>
          <textarea name="description" value={formData.description} onChange={handleChange} rows="3" style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
        </div>

        <button type="submit" disabled={submitting} style={{ width: "100%", padding: 10, backgroundColor: submitting ? "#93c5fd" : "#2563eb", color: "#fff", border: "none", borderRadius: 6, cursor: submitting ? "not-allowed" : "pointer", fontWeight: 600 }}>
          {submitting ? "Saving..." : "Save Property"}
        </button>
      </form>
    </div>
  );
}
