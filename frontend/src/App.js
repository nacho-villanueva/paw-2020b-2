import NavBar from "./components/NavBar.js";
import LandingPage from "./components/LandingPage.js";
import "./App.css";
import "./css/bootstrap-related.css";

import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

function App() {
  return (
    <Router>
      <div className="main-container">
      <NavBar />
        <div className="wrapper">
          <Switch>
            <Route path="/" exact component={LandingPage}/>
          </Switch>
        </div>
      </div>
    </Router>
  );
}

export default App;
