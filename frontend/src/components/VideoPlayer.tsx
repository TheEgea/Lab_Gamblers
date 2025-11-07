// frontend/src/components/VideoPlayer.tsx
import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import videoService, { Video } from '../services/VideoService';

const VideoPlayer = () => {
    const { videoId } = useParams<{ videoId: string }>();
    const navigate = useNavigate();
    const [video, setVideo] = useState<Video | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!videoId) {
            setError('ID de video no válido');
            setLoading(false);
            return;
        }

        videoService
            .getVideoById(videoId)
            .then((data) => {
                setVideo(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error('Error al cargar video:', err);
                setError(err.message || 'Error al cargar el video');
                setLoading(false);
            });
    }, [videoId]);

    const formatNumber = (num: number): string => {
        if (num >= 1000000) return `${(num / 1000000).toFixed(1)}M`;
        if (num >= 1000) return `${(num / 1000).toFixed(1)}K`;
        return num.toString();
    };

    const formatDuration = (seconds: number): string => {
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    };

    const formatDate = (dateString: string): string => {
        return new Date(dateString).toLocaleDateString('es-ES', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
        });
    };

    if (loading) {
        return (
            <div className="container mt-4">
                <div className="text-center">
                    <div className="spinner-border" role="status">
                        <span className="visually-hidden">Cargando...</span>
                    </div>
                    <p className="mt-2">Cargando video...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container mt-4">
                <div className="alert alert-danger" role="alert">
                    <h4 className="alert-heading">Error</h4>
                    <p>{error}</p>
                    <button onClick={() => navigate('/')} className="btn btn-primary">
                        Volver al inicio
                    </button>
                </div>
            </div>
        );
    }

    if (!video) {
        return (
            <div className="container mt-4">
                <div className="alert alert-warning" role="alert">
                    Video no encontrado
                </div>
            </div>
        );
    }

    return (
        <div className="container-fluid mt-3">
            <div className="row">
                {/* Columna principal - Reproductor */}
                <div className="col-lg-8">
                    {/* Reproductor de video */}
                    <div className="ratio ratio-16x9 mb-3">
                        <video
                            controls
                            autoPlay
                            preload="metadata"
                            poster={videoService.getThumbnailUrl(video.id)}
                            className="rounded"
                            style={{ backgroundColor: '#000' }}
                        >
                            <source src={videoService.getVideoStreamUrl(video.id)} type="video/mp4" />
                            Tu navegador no soporta la reproducción de video HTML5.
                        </video>
                    </div>

                    {/* Información del video */}
                    <div className="mb-3">
                        <h1 className="fs-4 fw-bold mb-2">{video.title}</h1>

                        <div className="d-flex align-items-center justify-content-between mb-3">
                            <div className="d-flex align-items-center">
                <span className="text-muted me-3">
                  {formatNumber(video.viewCount)} visualizaciones
                </span>
                                <span className="text-muted me-3">·</span>
                                <span className="text-muted">{formatDate(video.createdAt)}</span>
                            </div>

                            <div className="d-flex align-items-center">
                                <button className="btn btn-outline-secondary me-2">
                                    <i className="bi bi-hand-thumbs-up me-1"></i>
                                    {formatNumber(video.likeCount)}
                                </button>
                                <button className="btn btn-outline-secondary me-2">
                                    <i className="bi bi-share me-1"></i>
                                    Compartir
                                </button>
                                <button className="btn btn-outline-secondary" onClick={() => navigate('/')}>
                                    <i className="bi bi-arrow-left me-1"></i>
                                    Volver
                                </button>
                            </div>
                        </div>

                        <hr />

                        {/* Información del canal */}
                        <div className="d-flex align-items-start mb-3">
                            <div
                                className="bg-secondary rounded-circle d-flex align-items-center justify-content-center me-3"
                                style={{ width: '50px', height: '50px' }}
                            >
                <span className="text-white fw-bold">
                  {video.channel
                      ? video.channel.charAt(0).toUpperCase()
                      : video.user.charAt(0).toUpperCase()}
                </span>
                            </div>
                            <div className="flex-grow-1">
                                <h6 className="mb-1 fw-bold">{video.channel || video.user}</h6>
                                <p className="text-muted small mb-0">
                                    {formatDuration(video.durationSeconds)} · {video.width}x{video.height}
                                </p>
                            </div>
                            <button className="btn btn-danger btn-sm">Suscribirse</button>
                        </div>

                        {/* Descripción */}
                        <div className="bg-light p-3 rounded">
                            <div className="d-flex mb-2">
                <span className="text-muted me-3">
                  {formatNumber(video.viewCount)} visualizaciones
                </span>
                                <span className="text-muted">{formatDate(video.createdAt)}</span>
                            </div>
                            <div style={{ whiteSpace: 'pre-wrap' }}>
                                {video.description || 'No hay descripción disponible.'}
                            </div>
                        </div>
                    </div>
                </div>

                {/* Columna lateral - Videos relacionados */}
                <div className="col-lg-4">
                    <h5 className="mb-3">Videos relacionados</h5>
                    <div className="text-muted text-center">Próximamente...</div>
                </div>
            </div>
        </div>
    );
};

export default VideoPlayer;
