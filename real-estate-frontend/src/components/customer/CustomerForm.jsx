// src/components/CustomerForm.jsx
import { useState, useEffect } from "react";

const TYPES_URL = "http://localhost:8080/api/customers/types";
const SAVE_URL = "http://localhost:8080/api/customers";

export default function CustomerForm() {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    phone: "",
    customerType: "",
  });

  const [customerTypes, setCustomerTypes] = useState([]);
  const [loadingTypes, setLoadingTypes] = useState(true);
  const [typesError, setTypesError] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  
  useEffect(() => {
    let mounted = true;

    const fetchCustomerTypes = async () => {
      try {
        setLoadingTypes(true);
        setTypesError(null);

        const res = await fetch(TYPES_URL);
        if (!res.ok) {
          throw new Error(`Failed to fetch types (${res.status})`);
        }
        const data = await res.json();
        if (!mounted) return;

        // data'nin string[] veya object[] olma ihtimaline göre güvenli dönüşüm
        setCustomerTypes(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error("Error fetching customer types:", err);
        if (mounted) setTypesError("Seçenekler yüklenemedi. Sayfayı yenileyin.");
      } finally {
        if (mounted) setLoadingTypes(false);
      }
    };

    fetchCustomerTypes();

    return () => {
      mounted = false;
    };
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Basit client-side validation: customerType seçilmiş mi?
    if (!formData.customerType) {
      alert("Lütfen müşteri tipini seçin.");
      return;
    }

    try {
      setSubmitting(true);
      const res = await fetch(SAVE_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      // Backend daha anlamlı mesaj döndürüyorsa onu kullan
      if (!res.ok) {
        let errMsg = "Server error";
        try {
          const errBody = await res.json();
          errMsg = errBody.message || JSON.stringify(errBody);
        } catch (parseErr) {
          // ignore parse error
        }
        throw new Error(errMsg);
      }

      const saved = await res.json();
      console.log("Saved in backend:", saved);
      alert("Customer saved successfully!");

      // temizle
      setFormData({
        firstName: "",
        lastName: "",
        phone: "",
        customerType: "",
      });
    } catch (err) {
      console.error("Error submitting form:", err);
      alert(`Kaydetme başarısız: ${err.message}`);
    } finally {
      setSubmitting(false);
    }
  };

  // helper: option label/value çıkarımı (string veya object için)
  const renderOption = (type) => {
    if (typeof type === "string") {
      return { value: type, label: type };
    }
    // eğer objeyse olası alanları dene
    const value = type.id ?? type.value ?? type.code ?? JSON.stringify(type);
    const label = type.name ?? type.label ?? type.value ?? value;
    return { value, label };
  };

  return (
    <div style={{ maxWidth: 400, margin: "0 auto", background: "#fff", padding: 20, borderRadius: 8, boxShadow: "0 2px 6px rgba(0,0,0,0.1)" }}>
      <h2 style={{ marginBottom: 20, textAlign: "center", fontSize: 20 }}>Müşteri Kaydı</h2>
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: 12 }}>
            <label style={{ display: "block", marginBottom: 6 }}>İsim *</label>
            <input
              type="text"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
              required
              style={{ width: "95%", padding: "8px 10px", border: "1px solid #d1d5db", borderRadius: 6 }}
            />
          </div>

          <div style={{ marginBottom: 12 }}>
            <label style={{ display: "block", marginBottom: 6 }}>Soyisim *</label>
            <input
              type="text"
              name="lastName"
              value={formData.lastName}
              onChange={handleChange}
              required
              style={{ width: "95%", padding: "8px 10px", border: "1px solid #d1d5db", borderRadius: 6 }}
            />
          </div>

          

          <div style={{ marginBottom: 12 }}>
            <label style={{ display: "block", marginBottom: 6 }}>Telefon *</label>
            <input
              type="text"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              required
              style={{ width: "95%", padding: "8px 10px", border: "1px solid #d1d5db", borderRadius: 6 }}
            />
          </div>

          <div style={{ marginBottom: 18 }}>
            <label style={{ display: "block", marginBottom: 6 }}>Müşteri Tipi *</label>

            {loadingTypes ? (
              <div style={{ color: "#6b7280", fontSize: 14 }}>Loading types...</div>
            ) : typesError ? (
              <div style={{ color: "#dc2626", fontSize: 14 }}>{typesError}</div>
            ) : (
              <select
                name="customerType"
                value={formData.customerType}
                onChange={handleChange}
                required
                style={{ width: "100%", padding: "8px 10px", border: "1px solid #d1d5db", borderRadius: 6 }}
              >
                <option value="">Tip Seçin</option>
                {customerTypes.length === 0 && (
                  <option value="" disabled>No types available</option>
                )}
                {customerTypes.map((t) => {
                  const { value, label } = renderOption(t);
                  return <option key={value} value={value}>{label}</option>;
                })}
              </select>
            )}
          </div>

          <button
            type="submit"
            disabled={submitting}
            style={{
              width: "100%",
              padding: "10px",
              backgroundColor: submitting ? "#93c5fd" : "#2563eb",
              color: "white",
              border: "none",
              borderRadius: 6,
              cursor: submitting ? "not-allowed" : "pointer",
              fontWeight: 600
            }}
          >
            {submitting ? "Saving..." : "Save Customer"}
          </button>
        </form>
      </div>
  );
}
