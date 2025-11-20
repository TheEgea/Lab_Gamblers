// frontend/src/App.tsx
import './App.css';
import { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Link } from 'react-router-dom';
import Login from './components/Login';
import Signup from './components/Signup';
import AppBar from './components/AppBar';
import Home from './components/Home';
import VideoPlayer from './components/VideoPlayer';
import ChannelPage from "./components/ChannelPage.tsx";
import Profile from "./components/Profile.tsx";
import Subscriptions from "./components/Subscriptions.tsx";

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

    const handleLogout = () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
        setIsAuth(false);
    };

    const user = {
        username: "MiUsuario",
        avatarUrl: "",
        subscriptions: [] as { id: string; name: string; avatarUrl?: string }[],
    };

    return (
        <Router>
            <div className="App">
                {!isAuth ? (
                    <>
                        <AppBar showLogin={showLogin} setShowLogin={setShowLogin} />
                        <div className="App-content">
                            <img src="/logo.jpg" className="App-logo" alt="logo" style={{ maxWidth: '200px', margin: '20px auto' }} />
                            {showLogin ? (
                                <Login onLoginSuccess={handleLoginSuccess} />
                            ) : (
                                <Signup onSignupSuccess={handleLoginSuccess} />
                            )}
                        </div>
                    </>
                ) : (
                    <>
                        <nav className="navbar navbar-dark bg-dark">
                            <div className="container-fluid">
                                <div className="d-flex align-items-center">
                                    <Link className="navbar-brand" to="/">ProTube</Link>
                                    <Link
                                        to="/subscriptions"
                                        className="text-white text-decoration-none ms-3"
                                        style={{ fontSize: '1rem' }}
                                    >
                                        Suscripciones
                                    </Link>
                                </div>
                                <div className="d-flex align-items-center">
                                    <Link
                                        to="/profile"
                                        className="btn btn-outline-light btn-sm me-2"
                                    >
                                        Perfil
                                    </Link>
                                    <button
                                        className="btn btn-outline-light btn-sm"
                                        onClick={handleLogout}
                                    >
                                        Cerrar Sesión
                                    </button>
                                </div>
                            </div>
                        </nav>
                        <Routes>
                            <Route path="/" element={<Home />} />
                            <Route path="/video/:videoId" element={<VideoPlayer />} />
                            <Route path="/channel/:channelName" element={<ChannelPage />} />
                            <Route path="/profile" element={<Profile />} />
                            <Route path="/subscriptions" element={<Subscriptions />} />
                            <Route path="*" element={<Navigate to="/" replace />} />
                        </Routes>
                    </>
                )}
            </div>
        </Router>
    );
}

export default App;
