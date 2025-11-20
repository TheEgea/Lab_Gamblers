import React, { useEffect, useState } from 'react';
import { SubscriptionService, UserProfileResponse } from '../services/SubscriptionService';

const Profile: React.FC = () => {
    const [profile, setProfile] = useState<UserProfileResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const loadProfile = async () => {
            try {
                setLoading(true);
                setError(null);

                const data = await SubscriptionService.getUserProfile();
                setProfile(data);
            } catch (err) {
                console.error('Error al cargar el perfil:', err);
                setError(err?.message || 'Error al cargar el perfil');
            } finally {
                setLoading(false);
            }
        };

        loadProfile();
    }, []);

    if (loading) {
        return (
            <div
                className="d-flex justify-content-center align-items-center"
                style={{
                    minHeight: '60vh',
                }}
            >
                <div className="spinner-border text-warning" role="status">
                    <span className="visually-hidden">
                        Cargando...
                    </span>
                </div>
            </div>
        );
    }

    if (error || !profile) {
        return (
            <div className="container mt-4">
                <div className="alert alert-danger" role="alert">
                    {error || 'No se ha podido cargar el perfil.'}
                </div>
            </div>
        );
    }

    const username =
        typeof profile.username === 'string'
            ? profile.username
            : (profile.username as any)?.value ?? '';

    const initial =
        username && typeof username === 'string' && username.length > 0
            ? username.charAt(0).toUpperCase()
            : '?';

    const subscribedChannels = Array.isArray(profile.subscribedChannels)
        ? profile.subscribedChannels
        : [];

    return (
        <div className="container py-4">
            { /* Titulo principal */ }
            <div className="mb-4">
                <h2 className="text-light mb-1">Perfil</h2>
                <p className="text-muted mb-0" style={{ fontSize: '0.95rem' }}>
                    Información de tu cuenta y canales a los que estás suscrito.
                </p>
            </div>

            <div className="row g-4">
                { /* Columna izquierda: tarjeta de perfil */ }
                <div className="col-12 col-lg-4">
                    <div
                        className="card border-0 shadow-sm h-100"
                        style={{
                            backgroundColor: '#1f1f23',
                            color: '#f5f5f5',
                        }}
                    >
                        <div className="card-body d-flex flex-column align-items-center text-center">
                            { /* Avatar del usuario con su letra inicial */ }
                            <div
                                className="d-flex align-items-center justify-content-center rounded-circle mb-3"
                                style={{
                                    width: 96,
                                    height: 96,
                                    backgroundColor: '#ff6b35',
                                    color: 'white',
                                    fontSize: 40,
                                    fontWeight: 700,
                                }}
                            >
                                {initial}
                            </div>

                            <h3 className="mb-1" style={{ fontSize: "1.4rem" }}>
                                {username}
                            </h3>
                            <p className="text-muted mb-3" style={{ fontSize: "0.9rem" }}>
                                {profile.email}
                            </p>

                            <div className="d-flex justify-content-center w-100 mt-2">
                                <div className="px-3">
                                    <div style={{ color: '#e5e5e5' }}>
                                        Suscripciones
                                    </div>
                                    <div className="fw-semibold" style={{ fontSize: '1.1rem' }}>
                                        {profile.subscriptionCount ?? 0}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div
                            className="card-footer border-0"
                            style={{
                                backgroundColor: '#18181b',
                                fontSize: '0.85rem',
                            }}
                        >
                            <span style={{ color: '#e5e5e5' }}>
                                Gestiona tus suscripciones desde la página de {' '}
                                <span style={{ color: '#ff6b35' }}>Suscripciones</span> o desde los vídeos.
                            </span>
                        </div>
                    </div>
                </div>

                { /* Columna derecha: lista de canales suscritos */ }
                <div className="col-12 col-lg-8">
                    <div
                        className="card border-0 shadow-sm h-100"
                        style={{
                            backgroundColor: '#18181b',
                            color: '#f5f5f5',
                        }}
                    >
                        <div className="card-body">
                            <div className="d-flex align-items-center justify-content-between mb-3">
                                <h4 className="mb-0" style={{ fontSize: '1.2rem' }}>
                                    Canales suscritos
                                </h4>
                                <span
                                    className="badge rounded-pill"
                                    style={{
                                        backgroundColor: '#2f3136',
                                        fontSize: '0.8rem',
                                    }}
                                >
                                    {subscribedChannels.length} canal
                                    {subscribedChannels.length === 1 ? '' : 'es'}
                                </span>
                            </div>

                            {subscribedChannels.length === 0 ? (
                                <p className="text-muted mb-0" style={{ fontSize: '0.9rem' }}>
                                    Todavía no estás suscrito a ningún canal. Explora vídeos y usa el botón de{' '}
                                    <span style={{ color: '#ff6b35' }}>Suscribirse</span> para seguir a tus creadores de contenido favoritos.
                                </p>
                            ) : (
                                <ul className="list-group list-group-flush" style={{ background: 'transparent' }}>
                                    {subscribedChannels.map((channel) => (
                                        <li
                                            key={channel}
                                            className="list-group-item d-flex justify-content-between align-items-center px-0"
                                            style={{
                                                backgroundColor: 'transparent',
                                                borderColor: '#2f3136',
                                                color: '#e5e5e5',
                                            }}
                                        >
                                            <span>{channel}</span>
                                            <span
                                                className="badge rounded-pill"
                                                style={{
                                                    backgroundColor: '#2f3136',
                                                    fontSize: '0.75rem',
                                                    color: '#ffb199',
                                                }}
                                            >
                                                Canal suscrito
                                            </span>
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Profile;
