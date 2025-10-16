    import { useState, useEffect } from "react";

    const PROPERTIES_URL = "http://localhost:8080/api/property-searches/search";

    export default function PropertySearchPage() {
    const [formData, setFormData] = useState({
        customerId: "",
        propertyType: "",
        minPrice: "",
        maxPrice: "",
        minSquareMeters: "",
        maxSquareMeters: "",
        roomCount: "",
        heatingType: "",
        status: ""
    });

    const [searchResults, setSearchResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [selectedProperty, setSelectedProperty] = useState(null);
    const [customers, setCustomers] = useState([]);
    const [loadingCustomers, setLoadingCustomers] = useState(true);
    const [customerError, setCustomerError] = useState(null);


    const roomOptions = ["1+0", "1+1", "2+1", "3+1", "4+1", "5+1"];
    const statusOptions = ["FOR_SALE", "FOR_RENT", "SOLD", "RENTED"];

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
      
        try {
          const payload = {
            propertyType: formData.propertyType || null,
            minPrice: formData.minPrice ? parseFloat(formData.minPrice) : null,
            maxPrice: formData.maxPrice ? parseFloat(formData.maxPrice) : null,
            minSquareMeters: formData.minSquareMeters ? parseInt(formData.minSquareMeters) : null,
            maxSquareMeters: formData.maxSquareMeters ? parseInt(formData.maxSquareMeters) : null,
            roomCount: formData.roomCount || null,
            heatingType: formData.heatingType || null,
            status: formData.status || null,
          };
      
          const res = await fetch(PROPERTIES_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
          });
      
          if (!res.ok) throw new Error("Arama başarısız");
      
          const data = await res.json();
          setSearchResults(data);
        } catch (err) {
          alert(`Arama sırasında hata: ${err.message}`);
        } finally {
          setLoading(false);
        }
      };
      

    const handleSaveSearch = async () => {
        try {
        const payload = {
            propertyType: formData.propertyType,
            minPrice: formData.minPrice ? parseFloat(formData.minPrice) : null,
            maxPrice: formData.maxPrice ? parseFloat(formData.maxPrice) : null,
            minSquareMeters: formData.minSquareMeters ? parseInt(formData.minSquareMeters) : null,
            maxSquareMeters: formData.maxSquareMeters ? parseInt(formData.maxSquareMeters) : null,
            roomCount: formData.roomCount || null,
            heatingType: formData.heatingType || null,
            status: formData.status || null,
            customerId: formData.customerId // burada UUID gönderiyoruz
        };
    
        const res = await fetch("http://localhost:8080/api/saved-searches", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
    
        if (!res.ok) throw new Error("Kayıt başarısız");
        alert("Arama kriterleri kaydedildi");
    
        } catch (err) {
        alert(`Kayıt sırasında hata: ${err.message}`);
        }
    };
    

    useEffect(() => {
        const fetchCustomers = async () => {
          try {
            setLoadingCustomers(true);
            const res = await fetch("http://localhost:8080/api/customers");
            if (!res.ok) throw new Error("Müşteriler yüklenemedi");
            const data = await res.json();
            setCustomers(data);
          } catch (err) {
            setCustomerError(err.message);
          } finally {
            setLoadingCustomers(false);
          }
        };
        fetchCustomers();
      }, []);
    
    

    const handlePrint = () => {
        window.print();
    };

    return (
        <div style={{ maxWidth: 500, margin: "0 auto", padding: 20, background: "#fff", borderRadius: 8, boxShadow: "0 2px 6px rgba(0,0,0,0.1)" }}>
        <h2 style={{ textAlign: "center", marginBottom: 20 }}>Emlak Arama</h2>
        
        {/* Arama Formu */}
        <form onSubmit={handleSearch} style={{ display: "grid", gap: 12, marginBottom: 20 }}>
        <div>
            <label>Müşteri</label>
            {loadingCustomers ? (
                <p>Yükleniyor...</p>
            ) : customerError ? (
                <p style={{ color: "red" }}>{customerError}</p>
            ) : (
                <select
                name="customerId"
                value={formData.customerId}
                onChange={(e) => setFormData(prev => ({ ...prev, customerId: e.target.value }))}
                style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }}
                >
                <option value="">Seçiniz</option>
                {customers.map(c => (
                    <option key={c.id} value={c.id}>
                    {c.firstName} {c.lastName}
                    </option>
                ))}
                </select>
            )}
            </div>

            <div>
            <label>Emlak Tipi</label>
            <input type="text" name="propertyType" value={formData.propertyType} onChange={handleChange} style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
            </div>

            <div>
            <label>Min Fiyat</label>
            <input type="number" name="minPrice" value={formData.minPrice} onChange={handleChange} style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
            </div>

            <div>
            <label>Max Fiyat</label>
            <input type="number" name="maxPrice" value={formData.maxPrice} onChange={handleChange} style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
            </div>

            <div>
            <label>Min Metrekare</label>
            <input type="number" name="minSquareMeters" value={formData.minSquareMeters} onChange={handleChange} style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
            </div>

            <div>
            <label>Max Metrekare</label>
            <input type="number" name="maxSquareMeters" value={formData.maxSquareMeters} onChange={handleChange} style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
            </div>

            <div>
            <label>Oda Sayısı</label>
            <select name="roomCount" value={formData.roomCount} onChange={handleChange} style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }}>
                <option value="">Seçiniz</option>
                {roomOptions.map(r => <option key={r} value={r}>{r}</option>)}
            </select>
            </div>

            <div>
            <label>Durum</label>
            <select name="status" value={formData.status} onChange={handleChange} style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }}>
                <option value="">Seçiniz</option>
                {statusOptions.map(s => <option key={s} value={s}>{s}</option>)}
            </select>
            </div>

            <div>
            <label>Isınma Tipi</label>
            <input type="text" name="heatingType" value={formData.heatingType} onChange={handleChange} style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid #d1d5db" }} />
            </div>

            <div style={{ display: "flex", gap: 10 }}>
            <button
                type="submit"
                disabled={loading}
                style={{ flex: 1, padding: 10, backgroundColor: "#2563eb", color: "#fff", border: "none", borderRadius: 6, cursor: "pointer" }}
            >
                {loading ? "Aranıyor..." : "Ara"}
            </button>

            <button
                type="button"
                onClick={() => handleSaveSearch(formData)}
                style={{ flex: 1, padding: 10, backgroundColor: "#10b981", color: "#fff", border: "none", borderRadius: 6, cursor: "pointer" }}
            >
                Kaydet
            </button>
            </div>
        </form>

        {/* Arama Sonuçları */}
        <ul style={{ listStyle: "none", padding: 0 }}>
            {searchResults.map(prop => (
            <li
                key={prop.id}
                onClick={() => setSelectedProperty(prop)}
                style={{ cursor: "pointer", border: "1px solid #d1d5db", borderRadius: 6, padding: 12, marginBottom: 10, boxShadow: "0 1px 3px rgba(0,0,0,0.05)" }}
            >
                <strong>{prop.title}</strong> - {prop.status} - {prop.price}₺
            </li>
            ))}
        </ul>

        {/* Popup */}
        {selectedProperty && (
            <div style={{
            position: "fixed",
            top: 0, left: 0, right: 0, bottom: 0,
            backgroundColor: "rgba(0,0,0,0.5)",
            display: "flex", justifyContent: "center", alignItems: "center",
            zIndex: 1000
            }}>
            <div style={{ backgroundColor: "#fff", padding: 20, borderRadius: 8, maxWidth: 500, width: "90%", position: "relative" }}>
                
                {/* Sol üst çarpı */}
                <button
                onClick={() => setSelectedProperty(null)}
                style={{color:"black", textAlign: "left", fontSize: 18, border: "none", cursor: "pointer", background:"transparent"}}
                >
                x
                </button>

                <div   style={{marginLeft: 50}}>
                    <h2>{selectedProperty.title}</h2>
                    <p><strong>Adres:</strong> {selectedProperty.address}</p>
                    <p><strong>Fiyat:</strong> {selectedProperty.price}₺</p>
                    <p><strong>Durum:</strong> {selectedProperty.status}</p>
                    <p><strong>Oda Sayısı:</strong> {selectedProperty.roomCount}</p>
                    <p><strong>Metrekare:</strong> {selectedProperty.squareMeters} m²</p>
                    <p><strong>Isınma Tipi:</strong> {selectedProperty.heatingType}</p>
                    <p><strong>Açıklama:</strong> {selectedProperty.description}</p>
                </div>
                <button
                onClick={handlePrint}
                style={{
                    marginTop: 15,
                    padding: "8px 12px",
                    backgroundColor: "#2563eb",
                    color: "#fff",
                    border: "none",
                    borderRadius: 6,
                    cursor: "pointer"
                }}
                >
                Yazdır
                </button>
            </div>
            </div>
        )}
        </div>
    );
    }
