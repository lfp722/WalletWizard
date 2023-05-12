import { useEffect, useRef } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../styles/WelcomeStyle.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import anime from 'animejs';

function Welcome() {
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
      <h1 className="home-title ml2" ref={textWrapperRef}>Welcome to Wallet Wizard!</h1>
    </div>
  );
}

export default Welcome;
