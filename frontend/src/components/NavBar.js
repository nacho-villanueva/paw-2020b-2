import { Navbar, Button } from "react-bootstrap";
import {Link, useHistory} from "react-router-dom";
import "./Style/NavBar.css";
import "./../css/bootstrap-related.css";
import {useSelector} from "react-redux";
import {Roles} from "../constants/Roles";
import {StatusType} from "../redux/actions/actions";
import {logout} from "../api/Auth";

function NavBar() {
  const homepage = "/";
  const profile = "/profile";

  const history = useHistory();

  function navbarLogout(){
    logout();
    history.push("/");
  }

  const status = useSelector(state => state.auth.status);
  const role = useSelector(state => state.auth.role);

  return (
    <Navbar
      className="navbar-expand-lg fixed-top py-3 navbar bg-light"
      id="mainNav"
    >

      <div className="navbarContainer">
        <Link className="navbar-title text-primary" to={homepage}>
            <i className="fas fa-laptop-medical fa-lg icons"/>
            &nbsp;&nbsp;/&nbsp;&nbsp; MedTransfer
        </Link>

        <Button
          className="navbar-toggler navbar-toggler-right"
          type="button"
          data-toggle="collapse"
          data-target="#navbarResponsive"
          aria-controls="navbarResponsive"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"/>
        </Button>
          <ul className="navbar-nav ml-auto my-2 my-lg-0">
            {status === StatusType.AUTHENTICATED && role !== Roles.UNREGISTERED && <Link className="navbar-options" to={profile}>
              <li className="nav-item">
                  <i className="fas fa-lg fa-user-circle icons"/>
                  Profile
              </li>
            </Link>}
            {status === StatusType.AUTHENTICATED && role === Roles.UNREGISTERED && <Link onClick={navbarLogout} className="navbar-options" to={profile}>
              <li className="nav-item">
                Logout
              </li>
            </Link>}
          </ul>
      </div>
    </Navbar>
  );
}

export default NavBar;
