import React from 'react';

interface AppBarProps {
  showLogin: boolean;
  setShowLogin: (value: boolean) => void;
}

const AppBar: React.FC<AppBarProps> = ({ showLogin, setShowLogin }) => {
  return (
    <div style={{
      width: '100%',
      height: '60px',
      backgroundColor: '#1976d2',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      padding: '0 20px',
      boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
      position: 'fixed',
      top: 0,
      left: 0,
      zIndex: 1000,
    }}>
      <div style={{
        color: 'white',
        fontSize: '20px',
        fontWeight: 'bold',
      }}>
        ProTube
      </div>

      <div style={{ display: 'flex', gap: '10px' }}>
        <button
          onClick={() => setShowLogin(true)}
          style={{
            padding: '8px 20px',
            backgroundColor: showLogin ? 'white' : 'transparent',
            color: showLogin ? '#1976d2' : 'white',
            border: showLogin ? 'none' : '1px solid white',
            borderRadius: '4px',
            cursor: 'pointer',
            fontSize: '14px',
            fontWeight: showLogin ? 'bold' : 'normal',
          }}
        >
          Iniciar Sesión
        </button>

        <button
          onClick={() => setShowLogin(false)}
          style={{
            padding: '8px 20px',
            backgroundColor: !showLogin ? 'white' : 'transparent',
            color: !showLogin ? '#1976d2' : 'white',
            border: !showLogin ? 'none' : '1px solid white',
            borderRadius: '4px',
            cursor: 'pointer',
            fontSize: '14px',
            fontWeight: !showLogin ? 'bold' : 'normal',
          }}
        >
          Registrarse
        </button>
      </div>
    </div>
  );
};

export default AppBar;