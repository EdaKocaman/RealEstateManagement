// src/components/EmployeeForm.jsx
import { useState, useEffect } from "react";

const EMPLOYEES_URL = "http://localhost:8080/api/employees";
const COMPANIES_URL = "http://localhost:8080/api/companies";

export default function EmployeeForm() {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    phone: "",
    employeeRole: "",
    companyId: ""
  });

  const [companies, setCompanies] = useState([]);
  const [loadingCompanies, setLoadingCompanies] = useState(true);
  const [companiesError, setCompaniesError] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  // Employee role seçenekleri
  const employeeRoles = ["STAFF", "ADMIN"];

  // Şirket listesini backend'den çek
  useEffect(() => {
    let mounted = true;

    const fetchCompanies = async () => {
      try {
        setLoadingCompanies(true);
        setCompaniesError(null);
        const res = await fetch(COMPANIES_URL);
        if (!res.ok) throw new Error(`Failed to fetch companies (${res.status})`);
        const data = await res.json();
        if (!mounted) return;
        setCompanies(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error(err);
        if (mounted) setCompaniesError("Şirketler yüklenemedi. Sayfayı yenileyin.");
      } finally {
        if (mounted) setLoadingCompanies(false);
      }
    };

    fetchCompanies();
    return () => { mounted = false; };
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.companyId) {
      alert("Lütfen şirket seçin.");
      return;
    }
    if (!formData.employeeRole) {
      alert("Lütfen rol seçin.");
      return;
    }

    try {
      setSubmitting(true);
      const res = await fetch(EMPLOYEES_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData)
      });
      if (!res.ok) {
        let errMsg = "Server error";
        try { 
          const errBody = await res.json(); 
          errMsg = errBody.message || JSON.stringify(errBody);
        } catch (parseErr) {}
        throw new Error(errMsg);
      }

      const saved = await res.json();
      console.log("Saved:", saved);
      alert("Employee saved successfully!");

      setFormData({
        firstName: "",
        lastName: "",
        phone: "",
        employeeRole: "",
        companyId: ""
      });
    } catch (err) {
      console.error(err);
      alert(`Kaydetme başarısız: ${err.message}`);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: "0 auto", background: "#fff", padding: 20, borderRadius: 8, boxShadow: "0 2px 6px rgba(0,0,0,0.1)" }}>
      <h2 style={{marginBottom: 20, textAlign: "center", fontSize: 20 }}>Emlakçı Kaydı</h2>
      <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: 15 }}>
            <label style={{ display: "block", marginBottom: 6 }}>First Name *</label>
            <input
              type="text"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
              required
              style={{ width: "95%", padding: 8, border: "1px solid #d1d5db", borderRadius: 6 }}
            />
          </div>

          <div style={{ marginBottom: 12 }}>
            <label style={{ display: "block", marginBottom: 6 }}>Last Name *</label>
            <input
              type="text"
              name="lastName"
              value={formData.lastName}
              onChange={handleChange}
              required
              style={{ width: "95%", padding: 8, border: "1px solid #d1d5db", borderRadius: 6 }}
            />
          </div>

          <div style={{ marginBottom: 12 }}>
            <label style={{ display: "block", marginBottom: 6 }}>Phone *</label>
            <input
              type="text"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              required
              style={{ width: "95%", padding: 8, border: "1px solid #d1d5db", borderRadius: 6 }}
            />
          </div>

          {/* Company Dropdown */}
          <div style={{ marginBottom: 18 }}>
            <label style={{ display: "block", marginBottom: 6 }}>Company *</label>
            {loadingCompanies ? (
              <div style={{ color: "#6b7280", fontSize: 14 }}>Loading companies...</div>
            ) : companiesError ? (
              <div style={{ color: "#dc2626", fontSize: 14 }}>{companiesError}</div>
            ) : (
              <select
              name="companyId"
              value={formData.companyId}
              onChange={handleChange}
              required
              style={{ width: "100%", padding: 8, border: "1px solid #d1d5db", borderRadius: 6 }}
            >
              <option value="">Select Company</option>
              {companies.map(c => (
                <option key={c.companyId || c.id} value={c.companyId || c.id}>
                  {c.name}
                </option>
              ))}
            </select>
            )}
          </div>

          {/* Employee Role Dropdown */}
          <div style={{ marginBottom: 18 }}>
            <label style={{ display: "block", marginBottom: 6 }}>Role *</label>
            <select
              name="employeeRole"
              value={formData.employeeRole}
              onChange={handleChange}
              required
              style={{ width: "100%", padding: 8, border: "1px solid #d1d5db", borderRadius: 6 }}
            >
              <option value="">Select Role</option>
              {employeeRoles.map(r => (
                <option key={r} value={r}>{r}</option>
              ))}
            </select>
          </div>

          <button
            type="submit"
            disabled={submitting}
            style={{
              width: "100%",
              padding: 10,
              backgroundColor: submitting ? "#93c5fd" : "#2563eb",
              color: "white",
              border: "none",
              borderRadius: 6,
              cursor: submitting ? "not-allowed" : "pointer",
              fontWeight: 600
            }}
          >
            {submitting ? "Saving..." : "Save Employee"}
          </button>
        </form>
      </div>
  );
}
