import React, { useState } from 'react';
import ReactDOM from 'react-dom';

function Modal(props) {
  return ReactDOM.createPortal(
    <div className="modal">
      <div className="modal-content">
        <p>{props.message}</p>
        <button onClick={props.onClose}>Close</button>
      </div>
    </div>,
    document.getElementById('modal-root')
  );
}

function LoanResult() {
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');

  function handleShowModal(message) {
    setShowModal(true);
    setModalMessage(message);
  }

  function handleCloseModal() {
    setShowModal(false);
  }

  return (
    <div>
      <h1>My Component</h1>
      <button onClick={() => handleShowModal('Hello, world! This is a modal.')}>Show Modal</button>
      {showModal && <Modal message={modalMessage} onClose={handleCloseModal} />}
    </div>
  );
}

export default LoanResult;
