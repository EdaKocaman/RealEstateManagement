// App.jsx
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from "react-router-dom";
import CompanyForm from "./components/company/CompanyForm";
import EmployeeForm from "./components/employee/EmployeeForm";
import CustomerForm from "./components/customer/CustomerForm";
import PropertyForm from "./components/property/PropertyForm";
import SearchProperty from "./components/property/SearchProperty"; 
import CompanyDashboard from "./components/company/CompanyDashboard";
import EmployeeDashboard from "./components/employee/EmployeeDashboard"; 




export default function App() {
  return (
    <Router>
      <div style={{ paddingTop: "10px", marginBottom: "20px", padding: "10px", backgroundColor: "#333" }}>
       
       {/*
        <Link to="/CompanyForm" style={{ color: "white", marginRight: "15px", textDecoration: "none" }}>
          İşyeri
        </Link>
            
        <Link to="/EmployeeForm" style={{ color: "white", marginRight: "15px", textDecoration: "none" }}>
          Çalışan
        </Link>

        <Link to="/CustomerForm" style={{ color: "white", marginRight: "15px", textDecoration: "none"}}>
          Müşteri
        </Link>
      
          
        <Link to="/PropertyForm" style={{color: "white", marginRight: "15px", textDecoration: "none"}} >
          Emlak Kaydı
        </Link>

        <Link to="/SearchProperty" style={{color: "white", marginRight: "15px", textDecoration: "none"}}>
          Emlak Arama
        </Link>

        <Link to="/CompanyDashboard" style={{color: "white", marginRight: "15px", textDecoration: "none"}}>
          Company
           Dashboard
        </Link>
        <Link to="/EmployeeDashboard" style={{color: "white", marginRight: "15px", textDecoration: "none"}}>
          Employee Dashboard
        </Link> */}
      </div>


      <Routes>

        {/* Sistem açıldığında CompanyForm sayfasından başlasın */}
        <Route path="/" element={<CompanyForm />} />

        <Route path="/CompanyDashboard" element={<CompanyDashboard />} />

        <Route path="/EmployeeDashboard" element={<EmployeeDashboard/>}/>
  
        <Route path="/SearchProperty" element={<SearchProperty/>} />
        {/*<Route path="/" element={<CompanyForm />} />
        <Route path="/CompanyForm" element={<CompanyForm />} />
        <Route path="/EmployeeForm" element={<EmployeeForm />} />
        <Route path="/CustomerForm" element={<CustomerForm />} />
        <Route path="/PropertyForm" element={<PropertyForm />} />
        <Route path="/SearchProperty" element={<SearchProperty/>} />
        <Route path="/CompanyDashboard" element={<CompanyDashboard/>}/>
        
        */}
      </Routes>
    </Router>
  );
}
