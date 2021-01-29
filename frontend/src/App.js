import LandingPage from "./components/LandingPage.js";
import Dashboard from "./components/Dashboard";
import "./App.css";

import "./css/bootstrap-related.css";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

function App() {
  return (
    <Router>
      <div className="main-container">
        <div className="wrapper">
          <Switch>
            <Route path="/" exact component={LandingPage}/>
            <Route path="/dashboard" component={Dashboard}/>
          </Switch>
        </div>
      </div>
    </Router>
  );
}

export default App;
