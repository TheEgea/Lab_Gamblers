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
    const [relatedVideos, setRelatedVideos] = useState<Video[]>([]);
    const [showCopiedToast, setShowCopiedToast] = useState(false);
    const [isDescriptionExpanded, setIsDescriptionExpanded] = useState(false);

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
                return videoService.getAllVideos();
            })
            .then((allVideos) => {
                const filtered = allVideos.filter(v => v.id !== videoId);
                const shuffled = filtered.sort(() => Math.random() - 0.5);
                setRelatedVideos(shuffled.slice(0, 5));
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

    const formatCommentDate = (timestamp: number | string): string => {
        if (!timestamp) return '';

        let date: Date;

        try {
            if (typeof timestamp === 'number') {
                date = new Date(timestamp * 1000);
            } else if (typeof timestamp === 'string') {
                date = new Date(timestamp);
            } else {
                date = new Date(timestamp);
            }
        } catch (err) {
            console.error('Error parsing timestamp:', timestamp, error);
            return '';
        }

        if (isNaN(date.getTime())) {
            console.warn('Invalid timestamp:', timestamp);
            return '';
        }

        const now = new Date();
        const currentYear = now.getFullYear();
        const commentYear = date.getFullYear();

        if (commentYear === currentYear) {
            return date.toLocaleDateString('es-ES', {
                day: 'numeric',
                month: 'short',
            });
        } else {
            return date.toLocaleDateString('es-ES', {
                day: 'numeric',
                month: 'short',
                year: 'numeric',
            });
        }
    };

    const handleShare = async () => {
        try {
            const currentUrl = window.location.href;
            await navigator.clipboard.writeText(currentUrl);

            setShowCopiedToast(true);

            setTimeout(() => {
                setShowCopiedToast(false);
            }, 2500);
        } catch (err) {
            console.error('Error al copiar al portapapeles:', err);
            fallbackCopyToClipboard(window.location.href);
        }
    };

    const fallbackCopyToClipboard = (text: string) => {
        const textArea = document.createElement('textarea');
        textArea.value = text;
        textArea.style.position = 'fixed';
        textArea.style.left = '-9999px';
        document.body.appendChild(textArea);
        textArea.select();

        try {
            document.execCommand('copy');
            setShowCopiedToast(true);
            setTimeout(() => setShowCopiedToast(false), 2500);
        } catch (err) {
            console.error('Fallback copy failed:', err);
        }

        document.body.removeChild(textArea);
    };

    if (loading) {
        return (
            <div className="container mt-4">
                <div className="text-center">
                    <div
                        className="spinner-border"
                        role="status"
                        style={{ color: '#ff6b35' }}
                    >
                        <span className="visually-hidden">Cargando...</span>
                    </div>
                    <p className="mt-2" style={{ color: '#e0e0e0' }}>Cargando video...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container mt-4">
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
    }

    if (!video) {
        return (
            <div className="container mt-4">
                <div
                    className="alert alert-warning"
                    role="alert"
                    style={{
                        backgroundColor: '#3d3520',
                        border: '1px solid #5c5330',
                        color: '#ffdb6b',
                    }}
                >
                    Video no encontrado
                </div>
            </div>
        );
    }

    return (
        <div
            className="container-fluid mt-3"
            style={{
                backgroundColor: '#1a1a1a',
                minHeight: '100vh',
                paddingTop: '80px',
            }}
        >
            <div className="row">
                {/* Columna principal - Reproductor */}
                <div className="col-lg-8">
                    {/* Reproductor de video */}
                    <div className="ratio ratio-16x9 mb-3">
                        <video
                            key={videoId}
                            controls
                            autoPlay
                            preload="metadata"
                            poster={videoService.getThumbnailUrl(videoId)}
                            className="rounded"
                            style={{ backgroundColor: '#000' }}
                        >
                            <source src={videoService.getVideoStreamUrl(videoId)} type="video/mp4" />
                            Tu navegador no soporta la reproducción de video HTML5.
                        </video>
                    </div>

                    {/* Información del video */}
                    <div className="mb-3">
                        <h1 className="fs-4 fw-bold mb-2" style={{ color: '#e0e0e0', textAlign: 'left' }}>
                            {video.title}
                        </h1>

                        <div className="d-flex align-items-center justify-content-between mb-3">
                            <div className="d-flex align-items-center">
                                <span style={{ color: '#a0a0a0', marginRight: '12px' }}>
                                    {formatNumber(video.viewCount)} visualizaciones
                                </span>
                                <span style={{ color: '#a0a0a0', marginRight: '12px' }}>·</span>
                                <span style={{ color: '#a0a0a0', marginRight: '12px' }}>{formatDate(video.createdAt)}</span>
                                <span style={{ color: '#a0a0a0', marginRight: '12px' }}>·</span>
                                <span
                                    style={{
                                        padding: '8px 16px',
                                        backgroundColor: 'transparent',
                                        color: '#e0e0e0',
                                        border: '1px solid #404040',
                                        borderRadius: '20px',
                                        cursor: 'pointer',
                                        display: 'flex',
                                        alignItems: 'center',
                                        gap: '6px',
                                        transition: 'all 0.2s',
                                    }}
                                    onMouseEnter={(e) => {
                                        e.currentTarget.style.backgroundColor = '#3a3a3a';
                                        e.currentTarget.style.borderColor = '#ff6b35';
                                    }}
                                    onMouseLeave={(e) => {
                                        e.currentTarget.style.backgroundColor = 'transparent';
                                        e.currentTarget.style.borderColor = '#404040';
                                    }}
                                >
                                    {formatNumber(video.likeCount)} Me Gusta
                                </span>
                            </div>

                            <div className="d-flex align-items-center gap-2">
                                <button
                                    onClick={handleShare}
                                    style={{
                                        padding: '8px 16px',
                                        backgroundColor: 'transparent',
                                        color: '#e0e0e0',
                                        border: '1px solid #404040',
                                        borderRadius: '20px',
                                        cursor: 'pointer',
                                        transition: 'all 0.2s',
                                    }}
                                    onMouseEnter={(e) => {
                                        e.currentTarget.style.backgroundColor = '#3a3a3a';
                                        e.currentTarget.style.borderColor = '#ff6b35';
                                    }}
                                    onMouseLeave={(e) => {
                                        e.currentTarget.style.backgroundColor = 'transparent';
                                        e.currentTarget.style.borderColor = '#404040';
                                    }}
                                >
                                    Compartir
                                </button>
                                <button
                                    onClick={() => navigate('/')}
                                    style={{
                                        padding: '8px 16px',
                                        backgroundColor: '#ff6b35',
                                        color: 'white',
                                        border: 'none',
                                        borderRadius: '20px',
                                        cursor: 'pointer',
                                        transition: 'all 0.2s',
                                    }}
                                    onMouseEnter={(e) => {
                                        e.currentTarget.style.backgroundColor = '#ff8555';
                                    }}
                                    onMouseLeave={(e) => {
                                        e.currentTarget.style.backgroundColor = '#ff6b35';
                                    }}
                                >
                                    ← Volver
                                </button>
                            </div>
                        </div>

                        {/* Información del canal */}
                        <div className="d-flex align-items-start mb-3">
                            <div
                                className="rounded-circle d-flex align-items-center justify-content-center me-3"
                                style={{
                                    width: '48px',
                                    height: '48px',
                                    backgroundColor: '#3a3a3a',
                                    cursor: 'pointer',
                                    transition: 'all 0.2s',
                                }}
                                onClick={() => navigate(`/channel/${encodeURIComponent(video.channel || video.user)}`)}
                                onMouseEnter={(e) => {
                                    e.currentTarget.style.backgroundColor = '#ff6b35';
                                }}
                                onMouseLeave={(e) => {
                                    e.currentTarget.style.backgroundColor = '#3a3a3a';
                                }}
                            >
                                <span style={{ color: '#ff6b35', fontWeight: 'bold', fontSize: '20px' }}>
                                  {video.channel
                                      ? video.channel.charAt(0).toUpperCase()
                                      : video.user.charAt(0).toUpperCase()}
                                </span>
                            </div>
                            <div className="flex-grow-1" style={{ textAlign: 'left' }}>
                                <h6
                                    className="mb-1 fw-bold"
                                    style={{
                                        color: '#e0e0e0',
                                        cursor: 'pointer',
                                    }}
                                    onClick={() => navigate(`/channel/${encodeURIComponent(video.channel || video.user)}`)}
                                    onMouseEnter={(e) => {
                                        e.currentTarget.style.color = '#ff6b35';
                                    }}
                                    onMouseLeave={(e) => {
                                        e.currentTarget.style.color = '#e0e0e0';
                                    }}
                                >
                                    {video.channel || video.user}
                                </h6>
                                <p style={{ color: '#a0a0a0', fontSize: '0.875rem', margin: 0 }}>
                                    {formatDuration(video.durationSeconds)}
                                </p>
                            </div>
                            <button
                                style={{
                                    padding: '10px 24px',
                                    backgroundColor: '#ff6b35',
                                    color: 'white',
                                    border: 'none',
                                    borderRadius: '20px',
                                    cursor: 'pointer',
                                    fontWeight: 'bold',
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

                        <hr style={{ borderColor: '#404040', opacity: 0.3 }} />

                        {/* Descripción */}
                        <div
                            onClick={() => setIsDescriptionExpanded(!isDescriptionExpanded)}
                            style={{
                                backgroundColor: '#2d2d2d',
                                padding: '16px',
                                borderRadius: '12px',
                                border: '1px solid #404040',
                                cursor: 'pointer',
                                transition: 'all 0.2s',
                                marginBottom: '24px',
                            }}
                            onMouseEnter={(e) => {
                                e.currentTarget.style.borderColor = '#ff6b35';
                            }}
                            onMouseLeave={(e) => {
                                e.currentTarget.style.borderColor = '#404040';
                            }}
                        >
                            <div className="d-flex mb-2">
                                <span style={{ color: '#a0a0a0', marginRight: '12px' }}>
                                  {formatNumber(video.viewCount)} visualizaciones
                                </span>
                                <span style={{ color: '#a0a0a0' }}>
                                    {formatDate(video.createdAt)}
                                </span>
                            </div>
                            <div
                                style={{
                                    whiteSpace: 'pre-wrap',
                                    textAlign: 'left',
                                    color: '#e0e0e0',
                                    lineHeight: '1.6',
                                    maxHeight: isDescriptionExpanded ? 'none' : '60px',
                                    overflow: 'hidden',
                                    position: 'relative',
                                }}
                            >
                                {video.description || 'No hay descripción disponible.'}
                            </div>
                            {!isDescriptionExpanded && (
                                <div
                                    style={{
                                        color: '#a0a0a0',
                                        fontSize: '0.875rem',
                                        marginTop: '8px',
                                        fontStyle: 'italic',
                                    }}
                                >
                                    Pulsa para ver toda la descripción...
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Sección de comentarios */}
                    <div style={{ marginTop: '32px' }}>
                        <h5 style={{ color: '#e0e0e0', marginBottom: '20px', fontWeight: 'bold' }}>
                            {video.comments?.length || 0} Comentarios
                        </h5>

                        {video.comments && video.comments.length > 0 ? (
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                                {video.comments.map((comment, index) => (
                                    <div
                                        key={index}
                                        style={{
                                            backgroundColor: '#2d2d2d',
                                            padding: '16px',
                                            borderRadius: '12px',
                                            border: '1px solid #404040',
                                            transition: 'all 0.2s',
                                            textAlign: 'left', /* TODO: Revisar si esta be posar aquesta linea, originalment no estava pensada per estar */
                                        }}
                                        onMouseEnter={(e) => {
                                            e.currentTarget.style.borderColor = '#505050';
                                        }}
                                        onMouseLeave={(e) => {
                                            e.currentTarget.style.borderColor = '#404040';
                                        }}
                                    >
                                        { /* Header del comentario: autor y fecha */ }
                                        <div style={{ display: 'flex', alignItems: 'center', marginBottom: '8px' }}>
                                            <span
                                                style={{
                                                    color: '#ff6b35',
                                                    fontSize: '1rem',
                                                    fontWeight: 'bold',
                                                    cursor: 'pointer',
                                                }}
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    navigate(`/channel/${encodeURIComponent(comment.usuario)}`);
                                                }}
                                                onMouseEnter={(e) => {
                                                    e.currentTarget.style.textDecoration = 'underline';
                                                }}
                                                onMouseLeave={(e) => {
                                                    e.currentTarget.style.textDecoration = 'none';
                                                }}
                                            >
                                                {comment.usuario}
                                            </span>
                                            <span
                                                style={{
                                                    color: '#707070',
                                                    fontSize: '0.8rem',
                                                    marginLeft: '12px',
                                                }}
                                            >
                                                {formatCommentDate(comment.timestamp)}
                                            </span>
                                        </div>

                                        { /* Texto del comentario */ }
                                        <div
                                            style={{
                                                color: '#e0e0e0',
                                                fontSize: '0.95rem',
                                                lineHeight: '1.5',
                                                marginBottom: '10px',
                                            }}
                                        >
                                            {comment.texto}
                                        </div>

                                        { /* Likes del comentario */ }
                                        <div
                                            style={{
                                                color: '#707070',
                                                fontSize: '0.8rem',
                                                display: 'flex',
                                                alignItems: 'center',
                                                gap: '6px',
                                            }}
                                        >
                                            <span>👍</span>
                                            <span>{formatNumber(comment.likes)}</span>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div
                                style={{
                                    backgroundColor: '#2d2d2d',
                                    padding: '32px',
                                    borderRadius: '12px',
                                    border: '1px solid #404040',
                                    textAlign: 'center',
                                }}
                            >
                                <p style={{ color: '#a0a0a0', margin: 0 }}>
                                    Aún no hay comentarios. ¡Sé el primero en comentar!
                                </p>
                            </div>
                        )}
                    </div>
                </div>

                {/* Columna lateral - Videos relacionados */}
                <div className="col-lg-4" style={{ paddingTop: '0' }}>
                    <h5 className="mb-3" style={{ color: '#e0e0e0' }}>Vídeos relacionados</h5>
                    {relatedVideos.length === 0 ? (
                        <div style={{ color: '#a0a0a0', textAlign: 'center' }}>Cargando...</div>
                    ) : (
                        <div className="d-flex flex-column gap-3" style={{ textAlign: 'left' }}>
                            {relatedVideos.map((relVideo) => (
                                <div
                                    key={relVideo.id}
                                    className="card flex-row p-2"
                                    style={{
                                        cursor: 'pointer' ,
                                        backgroundColor: '#2d2d2d',
                                        border: '1px solid #404040',
                                        transition: 'all 0.2s',
                                    }}
                                    onClick={() => navigate(`/video/${relVideo.id}`)}
                                    onMouseEnter={(e) => {
                                        e.currentTarget.style.borderColor = '#ff6b35';
                                        e.currentTarget.style.backgroundColor = '#3a3a3a';
                                    }}
                                    onMouseLeave={(e) => {
                                        e.currentTarget.style.borderColor = '#404040';
                                        e.currentTarget.style.backgroundColor = '#2d2d2d';
                                    }}
                                >
                                    <img
                                        src={videoService.getThumbnailUrl(relVideo.id)}
                                        alt={relVideo.title}
                                        className="rounded"
                                        style={{
                                            width: '168px',
                                            height: '94px',
                                            objectFit: 'cover',
                                            flexShrink: 0,
                                        }}
                                        onError={(e) => {
                                            e.currentTarget.src = 'https://via.placeholder.com/168x94?text=No+Thumb';
                                        }}
                                    />
                                    <div className="ms-2 flex-grow-1" style={{ minWidth: 0 }}>
                                        <h6
                                            className="card-title mb-1 small text-truncate"
                                            title={relVideo.title}
                                            style = {{
                                                color: '#e0e0e0',
                                                fontSize: '0.875rem',
                                                marginBottom: '4px',
                                                overflow: 'hidden',
                                                textOverflow: 'ellipsis',
                                                display: '-webkit-box',
                                                WebkitLineClamp: 2,
                                                WebkitBoxOrient: 'vertical',
                                                lineHeight: '1.3',
                                            }}
                                        >
                                            {relVideo.title}
                                        </h6>
                                        <p style={{
                                                color: '#a0a0a0',
                                                fontSize: '0.75rem',
                                                marginBottom: '2px',
                                        }}>
                                            {relVideo.user}
                                        </p>
                                        <p style={{
                                            color: '#707070',
                                            fontSize: '0.75rem',
                                            margin: 0,
                                        }}>
                                            {formatNumber(relVideo.viewCount)} visualizaciones
                                        </p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>

            {showCopiedToast && (
                <div
                    style={{
                        position: 'fixed',
                        bottom: '30px',
                        right: '30px',
                        backgroundColor: '#ff6b35',
                        color: 'white',
                        padding: '16px 24px',
                        borderRadius: '12px',
                        boxShadow: '0 4px 12px rgba(255, 107, 53, 0.4)',
                        zIndex: 9999,
                        display: 'flex',
                        alignItems: 'center',
                        gap: '10px',
                        animation: 'slideIn 0.3s ease-out',
                        fontSize: '0.95rem',
                        fontWeight: '500',
                    }}
                >
                    <span style={{ fontSize: '1.2rem' }}>✓</span>
                    <span>¡Enlace copiado al portapapeles!</span>
                </div>
            )}

            <style>{`
                @keyframes slideIn {
                    from {
                        transform: translateX(400px);
                        opacity: 0;
                    }
                    to {
                        transform: translateX(0);
                        opacity: 1;
                    }
                }
            `}</style>
        </div>
    );
};

export default VideoPlayer;
