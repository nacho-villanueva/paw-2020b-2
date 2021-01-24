import {Navbar, Button} from "react-bootstrap";
import "./../css/navbar.css";
import "./../css/bootstrap-related.css";

function NavBar() {
  const homepage = "/";
  const profile = "/profile";
  return (
    <Navbar className="navbar-expand-lg fixed-top py-3 navbar bg-light" id="mainNav">
      <div className="container">
        <a className="navbar-title text-primary" href={homepage}>
          <i className="fas fa-laptop-medical fa-lg icons"></i>&nbsp;&nbsp;/&nbsp;&nbsp;
          MedTransfer
        </a>
        <Button
          className="navbar-toggler navbar-toggler-right"
          type="button"
          data-toggle="collapse"
          data-target="#navbarResponsive"
          aria-controls="navbarResponsive"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </Button>
        <div className="container" id="navbarResponsive">
          <ul className="navbar-nav ml-auto my-2 my-lg-0">
            <li className="nav-item">
              <a className="navbar-options" href={profile}>
                <i className="fas fa-lg fa-user-circle icons"></i>
                Profile
              </a>
            </li>
          </ul>
        </div>
      </div>
    </Navbar>
  );
}

export default NavBar;
