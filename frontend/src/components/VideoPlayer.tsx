import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getEnv } from "../utils/Env";

interface Video {
    id: string;
    title: string;
    user: string;
    channel: string;
    description: string;
    viewCount: number;
    likeCount: number;
    durationSeconds: number;
    createdAt: string;
    width: number;
    height: number;
}

const VideoPlayer = () => {
    const { videoId } = useParams<{ videoId: string }>();
    const [video, setVideo] = useState<Video | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const env = getEnv();

    useEffect(() => {
        if (!videoId) {
            setError('ID de video no valid');
            setLoading(false);
            return;
        }

        // Obtenir metadades del video
        fetch(`${env.API_BASE_URL}/videos/${videoId}`)
            .then(async (res) => {
                if (!res.ok) {
                    throw new Error(`Error ${res.status}: ${res.statusText}`);
                }
                return res.json();
            })
            .then((data: Video) => {
                setVideo(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error('Error al carregar video:', err);
                setError(err.message);
                setLoading(false);
            });
    }, [videoId, env.API_BASE_URL]);

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
            day: 'numeric'
        });
    };

    if (loading) {
        return (
            <div className="container mt-4">
                <div className="text-center">
                    <div className="spinner-border" role="status">
                        <span className="visually-hidden">Carregant...</span>
                    </div>
                    <p className="mt-2">Carregant video...</p>
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
                </div>
            </div>
        );
    }

    if (!video) {
        return (
            <div className="container mt-4">
                <div className="alert alert-warning" role="alert">
                    Video no trobat
                </div>
            </div>
        );
    }

    return (
        <div className="container fluid mt-3">
            <div className="row">
                {/* Columna principal - Reproductor de video */}
                <div className="col-lg-8">
                    {/* Reproductor de video */}
                    <div className="ratio ratio-16x9 mb-3">
                        <video
                            controls
                            autoPlay
                            preload="metadata"
                            poster={`${env.MEDIA_BASE_URL}/thumbnails/{video.id}`}
                            className="rounded"
                            style={{ backgroundColor: '#000' }}
                        >
                            <source
                                src={`${env.MEDIA_BASE_URL}/video/{video.id}`}
                                type="video/mp4"
                            />
                            El teu navegador no suporta la reproducció de video HTML5.
                        </video>
                    </div>

                    {/* Informació del video */}
                    <div className="mb-3">
                        <h1 className="fs-4 fw-bold mb-2">{video.title}</h1>

                        <div className="d-flex align-items-center justify-content-between mb-3">
                            <div className="d-flex align-items-center">
                                <span className="text-muted me-3">
                                    {formatNumber(video.viewCount)} visualitzacions
                                </span>
                                <span className="text-muted me-3">·</span>
                                <span className="text-muted">
                                    {formatDate(video.createdAt)}
                                </span>
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
                                <button className="btn btn-outline-secondary">
                                    <i className="bi bi-three-dots"></i>
                                </button>
                            </div>
                        </div>

                        <hr />

                        {/* Informació del canal */}
                        <div className="d-flex align-items-start mb-3">
                            <div className="bg-secondary rounded-circle d-flex align-items-center justify-content-center me-3"
                                 style={{ width: '50px', height: '50px' }}>
                                <span className="text-white fw-bold">
                                    {video.channel ? video.channel.charAt(0).toUpperCase() : video.user.charAt(0).toUpperCase()}
                                </span>
                            </div>
                            <div className="flex-grow-1">
                                <h6 className="mb-1 fw-bold">
                                    {video.channel || video.user}
                                </h6>
                                <p className="text-muted small mb-0">
                                    Canal · {formatDuration(video.durationSeconds)} de duració
                                </p>
                            </div>
                            <button className="btn btn-danger btn-sm">
                                Subscriure's
                            </button>
                        </div>

                        {/* Descripció */}
                        <div className="bg-light p-3 rounded">
                            <div className="d-flex mb-2">
                                <span className="text-muted me-3">{formatNumber(video.viewCount)} visualitzacions</span>
                                <span className="text-muted">{formatDate(video.createdAt)}</span>
                            </div>
                            <div style={{ whiteSpace: 'pre-wrap' }}>
                                {video.description || 'No hi ha descripció disponible per a aquest video.'}
                            </div>
                        </div>
                    </div>
                </div>

                {/* Columna lateral - Videos Relacionats */}
                <div className="col-lg-4">
                    <h5 className="mb-3">Videos relacionats</h5>
                    <div className="text-muted text-center">
                        {/* Aqui posar VideoGrid */}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default VideoPlayer;