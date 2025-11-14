import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import videoService, { Video } from '../services/VideoService';
import VideoGrid from './VideoGrid';

const ChannelPage = () => {
    const { channelName } = useParams<{ channelName: string }>();
    const navigate = useNavigate();
    const [channelVideos, setChannelVideos] = useState<Video[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!channelName) {
            setError('Nombre de canal no válido');
            setLoading(false);
            return;
        }

        const decodedChannelName = decodeURIComponent(channelName);

        videoService
            .getAllVideos()
            .then((videos) => {
                const filtered = videos.filter(
                    (v) => v.channel === decodedChannelName || v.user === decodedChannelName
                );
                setChannelVideos(filtered);
                setLoading(false);
            })
            .catch((err) => {
                console.error('Error al cargar videos del canal:', err);
                setError(err.message || 'Error al cargar el canal');
                setLoading(false);
            });
    }, [channelName]);

    const formatNumber = (num: number): string => {
        if (num >= 1000000) return `${(num / 1000000).toFixed(1)}M`;
        if (num >= 1000) return `${(num / 1000).toFixed(1)}K`;
        return num.toString();
    };

    const getTotalViews = (): number => {
        return channelVideos.reduce((sum, video) => sum + video.viewCount, 0);
    };

    if (loading) {
        return (
            <div
                className="container mt-4"
                style={{
                    minHeight: '100vh',
                    paddingTop: '80px',
                    backgroundColor: '#1a1a1a',
                }}
            >
                <div className="text-center">
                    <div className="spinner-border" role="status" style={{ color: '#ff6b35'}}>
                        <span className="visually-hidden">Cargando...</span>
                    </div>
                    <p className="mt-2" style={{ color: '#e0e0e0' }}>Cargando canal...</p>
                </div>
            </div>
        );
    };

    if (error) {
        return (
            <div
                className="container mt-4"
                style={{
                    minHeight: '100vh',
                    paddingTop: '80px',
                    backgroundColor: '#1a1a1a',
                }}
            >
                <div
                    className="alert alert-danger"
                    role="alert"
                    style={{
                        backgroundColor: '#3d2020',
                        border: '1px solid #5c3030',
                        color: '#ff6b6b',
                    }}
                >
                    <h4 className="alert-heading">Error</h4>
                    <p>{error}</p>
                    <button
                        onClick={() => navigate('/')}
                        style={{
                            padding: '8px 20px',
                            backgroundColor: '#ff6b35',
                            color: 'white',
                            border: 'none',
                            borderRadius: '6px',
                            cursor: 'pointer',
                        }}
                    >
                        Volver al inicio
                    </button>
                </div>
            </div>
        );
    };

    const decodedChannelName = channelName ? decodeURIComponent(channelName) : 'Canal Desconocido';

    return (
        <div /* TODO: Esto quizá se podría eliminar, es la barra horizontal negra que hay encima del banner del canal
                      donde se veía toda la info del canal como el nombre y tal, si veis esto decidme algo y lo cambio */
            style={{
                backgroundColor: '#1a1a1a',
                minHeight: '100vh',
                paddingTop: '80px',
            }}
        >
            { /* Banner del canal */ }
            <div
                style={{
                    backgroundColor: '#2d2d2d',
                    borderBottom: '2px solid #ff6b35',
                    padding: '40px 0',
                }}
            >
                <div className="container">
                    <div className="row align-items-center">
                        { /* Avatar del canal */ }
                        <div className="col-auto">
                            <div
                                style={{
                                    width: '120px',
                                    height: '120px',
                                    borderRadius: '50%',
                                    backgroundColor: '#3a3a3a',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    border: '4px solid #ff6b35',
                                    fontSize: '48px',
                                    fontWeight: 'bold',
                                    color: '#ff6b35',
                                }}
                            >
                                {decodedChannelName.charAt(0).toUpperCase()}
                            </div>
                        </div>

                        { /* Información del canal */ }
                        <div className="col">
                            <h1
                                style={{
                                    color: '#e0e0e0',
                                    fontSize: '2.5rem',
                                    fontWeight: 'bold',
                                    marginBottom: '8px',
                                    alignItems: 'left', /* Si quitais esta linea y la de abajo, se centra el titulo */
                                    display: 'flex',    /* pero creo que queda peor */
                                }}
                            >
                                {decodedChannelName}
                            </h1>
                            <div
                                style={{
                                    color: '#a0a0a0',
                                    fontSize: '1rem',
                                    display: 'flex',
                                    gap: '20px',
                                    flexWrap: 'wrap',
                                }}
                            >
                                <span>{channelVideos.length} Videos</span>
                                <span>·</span>
                                <span>{formatNumber(getTotalViews())} visualizaciones totales</span>
                            </div>
                        </div>

                        { /* Botón de suscripción */ }
                        <div className="col-auto">
                            <button
                                style={{
                                    padding: '12px 32px',
                                    backgroundColor: '#ff6b35',
                                    color: 'white',
                                    border: 'none',
                                    borderRadius: '24px',
                                    cursor: 'pointer',
                                    fontWeight: 'bold',
                                    fontSize: '1rem',
                                    transition: 'all 0.2s',
                                }}
                                onMouseEnter={(e) => {
                                    e.currentTarget.style.backgroundColor = '#ff8555';
                                }}
                                onMouseLeave={(e) => {
                                    e.currentTarget.style.backgroundColor = '#ff6b35';
                                }}
                            >
                                Suscribirse
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            { /* Tabs del canal */ }
            <div
                style={{
                    backgroundColor: '#2d2d2d',
                    borderBottom: '1px solid #404040',
                }}
            >
                <div className="container">
                    <div
                        style={{
                            display: 'flex',
                            gap: '32px',
                            padding: '16px 0',
                        }}
                    >
                        <button
                            style={{
                                padding: '8px 0',
                                backgroundColor: 'transparent',
                                border: 'none',
                                borderBottom: '3px solid #ff6b35',
                                color: '#e0e0e0',
                                fontWeight: 'bold',
                                cursor: 'pointer',
                            }}
                        >
                            VIDEOS
                        </button>
                        <button
                            style={{
                                padding: '8px 0',
                                backgroundColor: 'transparent',
                                border: 'none',
                                borderBottom: '3px solid transparent',
                                color: '#a0a0a0',
                                cursor: 'pointer',
                            }}
                        >
                            ACERCA DE
                        </button>
                    </div>
                </div>
            </div>

            { /* Grid de videos del canal */ }
            <div className="container" style={{ padding: '32px 0' }}>
                {channelVideos.length === 0 ? (
                    <div
                        style={{
                            textAlign: 'center',
                            padding: '60px 20px',
                            backgroundColor: '#2d2d2d',
                            borderRadius: '12px',
                            border: '1px solid #404040',
                        }}
                    >
                        <h3 style={{ color: '#a0a0a0', marginBottom: '16px' }}>
                            Este canal aún no tiene vídeos.
                        </h3>
                        <button
                            onClick={() => navigate('/')}
                            style={{
                                padding: '10px 24px',
                                backgroundColor: '#ff6b35',
                                color: 'white',
                                border: 'none',
                                borderRadius: '20px',
                                cursor: 'pointer',
                            }}
                        >
                            Explorar otros vídeos
                        </button>
                    </div>
                ) : (
                    <VideoGrid
                        videos={channelVideos}
                        onVideoClick={(video) => navigate(`/video/${video.id}`)}
                    />
                )}
            </div>
        </div>
    );
};

export default ChannelPage;