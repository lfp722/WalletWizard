import { useEffect, useRef } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../styles/HomeStyle.css';
import anime from 'animejs';
import { Link } from 'react-router-dom';

function Home() {
  const textWrapperRef = useRef(null);

  useEffect(() => {
    const textWrapper = textWrapperRef.current;
    const text = textWrapper.textContent;

    textWrapper.innerHTML = text.replace(/\S/g, "<span class='letter'>$&</span>");

    anime.timeline({loop: true})
      .add({
        targets: '.ml2 .letter',
        scale: [4, 1],
        opacity: [0, 1],
        translateZ: 0,
        easing: "easeOutExpo",
        duration: 950,
        delay: (el, i) => 70*i
      }).add({
        targets: '.ml2',
        opacity: 0,
        duration: 1000,
        easing: "easeOutExpo",
        delay: 1000
      });
  }, []);

  return (
    <div className="home-container">
      <h1 className="home-title ml2" ref={textWrapperRef}>Wallet Wizard</h1>
      <p className="home-description">Please log in or register to continue.</p>
      <div className="button-container">
        <Link to="/login" className="btn btn-primary button">Log in</Link>
        <Link to="/register" className="btn btn-secondary button">Register</Link>
      </div>
    </div>
  );
}

export default Home;
