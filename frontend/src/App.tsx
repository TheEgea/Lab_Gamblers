import './App.css';
import { useState } from 'react';
import { useAllVideos } from './useAllVideos';
import Login from './components/Login';
import Signup from './components/Signup';
import AppBar from './components/AppBar';

function App() {
  const [showLogin, setShowLogin] = useState<boolean>(true);

  return (
    <div className="App">
      <AppBar showLogin={showLogin} setShowLogin={setShowLogin} />

      <div className="App-content">
        <img src="/protube-logo-removebg-preview.png" className="App-logo" alt="logo" />

        {showLogin ? <Login /> : <Signup />}
      </div>
    </div>
  );
}

// @ts-ignore
function ContentApp() {
  const { loading, message, value } = useAllVideos();
  switch (loading) {
    case 'loading':
      return <div>Loading...</div>;
    case 'error':
      return (
        <div>
          <h3>Error</h3> <p>{message}</p>
        </div>
      );
    case 'success':
      return (
        <>
          <strong>Videos available:</strong>
          <ul>
            {value.map((item) => (
              <li>{item}</li>
            ))}
          </ul>
        </>
      );
  }
  return <div>Idle...</div>;
}

export default App;