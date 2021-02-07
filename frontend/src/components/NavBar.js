import { Navbar, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import "./Style/NavBar.css";
import "./../css/bootstrap-related.css";

function NavBar() {
  const homepage = "/";
  const profile = "/profile";
  return (
    <Navbar
      className="navbar-expand-lg fixed-top py-3 navbar bg-light"
      id="mainNav"
    >
      <div className="container">
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
        <div className="container" id="navbarResponsive">
          <ul className="navbar-nav ml-auto my-2 my-lg-0">
            <Link className="navbar-options" to={profile}>
              <li className="nav-item">
                  <i className="fas fa-lg fa-user-circle icons"/>
                  Profile
              </li>
            </Link>
          </ul>
        </div>
      </div>
    </Navbar>
  );
}

export default NavBar;
