import './App.css';
import { useState, useEffect } from 'react';
import Login from './components/Login';
import Signup from './components/Signup';
import AppBar from './components/AppBar';
import Home from './components/Home';

function App() {
    const [showLogin, setShowLogin] = useState<boolean>(true);
    const [isAuth, setIsAuth] = useState<boolean>(false);

    // Verificar si ya estás logueado cuando carga la página
    useEffect(() => {
        const token = localStorage.getItem('authToken');
        setIsAuth(!!token);
    }, []);

    // Función que llama Login cuando es exitoso
    const handleLoginSuccess = () => {
        setIsAuth(true);
    };

    return (
        <div className="App">
            <AppBar showLogin={showLogin} setShowLogin={setShowLogin} />

            <div className="App-content">
                {!isAuth ? (
                    <>
                        <img src="/src/utils/logo.png" className="App-logo" alt="logo" />
                        {showLogin ? (
                            <Login onLoginSuccess={handleLoginSuccess} />
                        ) : (
                            <Signup onLoginSuccess={handleLoginSuccess} />
                        )}
                    </>
                ) : (
                    <Home />
                )}
            </div>
        </div>
    );
}

export default App;
