import LandingPage from "./components/LandingPage.js";
import DashboardPage from "./components/DashboardPage";
import "./App.css";

import "./css/bootstrap-related.css";
import { BrowserRouter as Router, Switch, Route, Redirect} from "react-router-dom";
import {useSelector} from "react-redux";
import {store} from "./redux";
import {StatusType} from "./redux/actions/actions";

function App() {
  const status = useSelector(state => state.auth.status);

  return (
    <Router>
      <div className="main-container">
        <div className="wrapper">
          <Switch>
            <Route path="/" exact>
              {status === StatusType.AUTHENTICATED ? <Redirect to="/dashboard" /> : <LandingPage />}
            </Route>
            {status === StatusType.AUTHENTICATED && <Route path="/dashboard" component={DashboardPage}/>}
            {status === StatusType.DE_AUTHENTICATED && <Redirect to={"/"} />}
            <Redirect from={"*"} to={"/"}/>
          </Switch>
        </div>
      </div>
    </Router>
  );
}

export default App;
