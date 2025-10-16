import { useState } from "react";

export default function SearchResults({ results }) {
  const [selectedProperty, setSelectedProperty] = useState(null);

  const handlePrint = () => {
    window.print();
  };

  return (
    <div>
      {/* Arama sonuçları */}
      <ul>
        {results.map(prop => (
          <li
            key={prop.id}
            onClick={() => setSelectedProperty(prop)}
            style={{ cursor: "pointer", borderBottom: "1px solid #ddd", padding: "8px" }}
          >
            {prop.title} - {prop.status} - {prop.price}₺
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
            {/* Close Button */}
            <button
              onClick={() => setSelectedProperty(null)}
              style={{ position: "absolute", top: 10, right: 10, fontWeight: "bold", fontSize: 18 }}
            >
              ×
            </button>

            {/* Property Details */}
            <h2>{selectedProperty.title}</h2>
            <p><strong>Adres:</strong> {selectedProperty.address}</p>
            <p><strong>Fiyat:</strong> {selectedProperty.price}₺</p>
            <p><strong>Durum:</strong> {selectedProperty.status}</p>
            <p><strong>Oda Sayısı:</strong> {selectedProperty.roomCount}</p>
            <p><strong>Metrekare:</strong> {selectedProperty.squareMeters} m²</p>
            <p><strong>Isınma Tipi:</strong> {selectedProperty.heatingType}</p>
            <p><strong>Açıklama:</strong> {selectedProperty.description}</p>

            {/* Print Button */}
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
