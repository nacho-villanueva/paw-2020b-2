import NavBar from "./components/NavBar.js";
import LandingPage from "./components/LandingPage.js";
import "./App.css";
import "./css/bootstrap-related.css";


function App() {
  return (
    <div className="main-container">
      <NavBar/>
      <LandingPage />
    </div>
  );
}

export default App;
