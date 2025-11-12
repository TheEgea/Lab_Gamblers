import React from 'react';

interface AppBarProps {
    showLogin: boolean;
    setShowLogin: (value: boolean) => void;
}

const AppBar: React.FC<AppBarProps> = ({showLogin, setShowLogin}) => {
    return (
        <div
            style={{
                width: '100%',
                height: '60px',
                backgroundColor: '#2d2d2d',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                padding: '0 20px',
                boxShadow: '0 2px 8px rgba(0,0,0,0.5)',
                position: 'fixed',
                top: 0,
                left: 0,
                zIndex: 1000,
                borderBottom: '1px solid #404040',
            }}
        >
            <div
                style={{
                    color: '#ff6b35',
                    fontSize: '24px',
                    fontWeight: 'bold',
                    cursor: 'pointer'
                }}
                onClick={() => window.location.href = '/'}
            >
                ProTube
            </div>

            <div style={{ display: 'flex', gap: '10px' }}>
                <button
                    onClick={() => setShowLogin(true)}
                    style={{
                        padding: '8px 20px',
                        backgroundColor: showLogin ? 'ff6b35' : 'transparent',
                        color: showLogin ? 'white' : 'e0e0e0',
                        border: showLogin ? 'none' : '1px solid #ff6b35',
                        borderRadius: '6px',
                        cursor: 'pointer',
                        fontSize: '14px',
                        fontWeight: showLogin ? 'bold' : 'normal',
                        transition: 'all 0.3s ease',
                    }}
                    onMouseEnter={(e) => {
                        if (!showLogin) {
                            e.currentTarget.style.backgroundColor = '#3a3a3a';
                        }
                    }}
                    onMouseLeave={(e) => {
                        if (!showLogin) {
                            e.currentTarget.style.backgroundColor = 'transparent';
                        }
                    }}
                >
                    Iniciar Sesión
                </button>

                <button
                    onClick={() => setShowLogin(false)}
                    style={{
                        padding: '8px 20px',
                        backgroundColor: !showLogin ? '#ff6b35' : 'transparent',
                        color: !showLogin ? 'white' : '#e0e0e0',
                        border: !showLogin ? 'none' : '1px solid #ff6b35',
                        borderRadius: '6px',
                        cursor: 'pointer',
                        fontSize: '14px',
                        fontWeight: !showLogin ? 'bold' : 'normal',
                        transition: 'all 0.3s ease',
                    }}
                    onMouseEnter={(e) => {
                        if (showLogin) {
                            e.currentTarget.style.backgroundColor = '#3a3a3a';
                        }
                    }}
                    onMouseLeave={(e) => {
                        if (showLogin) {
                            e.currentTarget.style.backgroundColor = 'transparent';
                        }
                    }}
                >
                    Registrarse
                </button>
            </div>
        </div>
    );
};

export default AppBar;