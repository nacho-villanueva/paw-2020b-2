import LandingPage from "./components/LandingPage.js";
import DashboardPage from "./components/DashboardPage";
import "./App.css";

import "./css/bootstrap-related.css";
import {HashRouter} from "react-router-dom";
import {BrowserRouter as Router, Switch, Route, Redirect} from "react-router-dom";
import {useSelector} from "react-redux";
import {store} from "./redux";
import {StatusType} from "./redux/actions/actions";
import {Roles} from "./constants/Roles";
import RegisterPage from "./components/registration/RegisterPage";

import ClinicSearch from "./components/ClinicSearch.js";

function App() {
  const status = useSelector(state => state.auth.status);
  const role = useSelector(state => state.auth.role);

  return (
    <HashRouter basename={"paw-2020b-2"}>
      <div className="main-container">
        <div className="wrapper">
          <Switch>

            <Route path="/user-verification">
              <LandingPage tokenValidation={true}/>
            </Route>

            <Route path ="/search-clinics">
              <ClinicSearch />
            </Route>

            <Route path="/" exact>
              {status === StatusType.AUTHENTICATED ? (role === Roles.UNREGISTERED ? <Redirect to={"/register"}/> : <Redirect to="/dashboard" />) : <LandingPage />}
            </Route>
            {status === StatusType.DE_AUTHENTICATED && <Redirect to={"/"} />}

            {status === StatusType.AUTHENTICATED && role !== Roles.UNREGISTERED && <Route path="/dashboard" component={DashboardPage}/>}
            {status === StatusType.AUTHENTICATED && role === Roles.UNREGISTERED && <Route path="/register" component={RegisterPage}/>}
            <Redirect from={"*"} to={"/"}/>
          </Switch>
        </div>
      </div>
    </HashRouter>
  );
}

export default App;
