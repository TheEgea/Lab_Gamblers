import React, { useEffect, useState } from 'react';
import { SubscriptionService, UserProfileResponse } from '../services/SubscriptionService';
import { useNavigate } from 'react-router-dom';

const Profile: React.FC = () => {
    const [profile, setProfile] = useState<UserProfileResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        loadProfile();
    }, []);

    const loadProfile = async () => {
        try {
            setLoading(true);
            setError(null);
            const userProfile = await SubscriptionService.getUserProfile();
            setProfile(userProfile);
        } catch (err) {
            console.error('Error loading profile:', err);
            setError('Error al cargar el perfil');
        } finally {
            setLoading(false);
        }
    };

    const handleChannelClick = (channelName: string) => {
        navigate(`/channel/${channelName}`);
    };

    if (loading) {
        return (
            <div className="container mt-5 text-center">
                <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Cargando...</span>
                </div>
            </div>
        );
    }

    if (error || !profile) {
        return (
            <div className="container mt-5">
                <div className="alert alert-danger" role="alert">
                    {error || 'Error al cargar el perfil'}
                </div>
            </div>
        );
    }

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-8">
                    <div className="card shadow-sm">
                        <div className="card-body">
                            <div className="text-center mb-4">
                                <div
                                    className="rounded-circle bg-primary text-white d-inline-flex align-items-center justify-content-center"
                                    style={{ width: '120px', height: '120px', fontSize: '48px', fontWeight: 'bold' }}
                                >
                                    {profile.username.charAt(0).toUpperCase()}
                                </div>
                            </div>

                            <h2 className="text-center mb-4">{profile.username}</h2>

                            <div className="mb-4">
                                <h5 className="text-muted">Información de la cuenta</h5>
                                <hr />
                                <div className="row">
                                    <div className="col-md-6 mb-3">
                                        <strong>Email:</strong>
                                        <p className="mb-0">{profile.email}</p>
                                    </div>
                                    <div className="col-md-6 mb-3">
                                        <strong>ID de Usuario:</strong>
                                        <p className="mb-0">#{profile.id}</p>
                                    </div>
                                </div>
                            </div>

                            <div className="mb-4">
                                <h5 className="text-muted">
                                    Suscripciones ({profile.subscriptionCount})
                                </h5>
                                <hr />
                                {profile.subscribedChannels.length === 0 ? (
                                    <p className="text-muted">No estás suscrito a ningún canal todavía.</p>
                                ) : (
                                    <div className="list-group">
                                        {profile.subscribedChannels.map((channel, index) => (
                                            <button
                                                key={index}
                                                className="list-group-item list-group-item-action d-flex align-items-center"
                                                onClick={() => handleChannelClick(channel)}
                                                style={{ cursor: 'pointer' }}
                                            >
                                                <div
                                                    className="rounded-circle bg-secondary text-white d-inline-flex align-items-center justify-content-center me-3"
                                                    style={{ width: '40px', height: '40px', fontSize: '18px' }}
                                                >
                                                    {channel.charAt(0).toUpperCase()}
                                                </div>
                                                <span>{channel}</span>
                                            </button>
                                        ))}
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Profile;
