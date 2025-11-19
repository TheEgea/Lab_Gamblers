// frontend/src/App.tsx
import './App.css';
import { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
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
    const handleRandomVideo = async () => {
        try {
            const response = await fetch('http://localhost:8080/videos/random');
            if (response.ok) {
                const video = await response.json();
                window.location.href = `/video/${video.id}`;
            } else {
                console.error('Failed to fetch random video');
            }
        } catch (error) {
            console.error('Error fetching random video:', error);
        }
    };

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
                                <a className="navbar-brand" href="/">ProTube</a>
                                <div style={{ display: 'flex', gap: '10px', marginLeft: 'auto' }}>
                                    <button
                                        onClick={handleRandomVideo}
                                        style={{
                                            padding: '8px 20px',
                                            backgroundColor: '#ff6b35',
                                            color: 'white',
                                            border: 'none',
                                            borderRadius: '6px',
                                            cursor: 'pointer',
                                            fontSize: '14px',
                                            fontWeight: 'bold',
                                            transition: 'all 0.3s ease',
                                        }}
                                    >
                                        Video Aleatorio
                                    </button>
                                    <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>
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
