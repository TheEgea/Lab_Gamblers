// frontend/src/components/VideoGrid.tsx
import React from 'react';
import { Video } from '../services/VideoService';
import videoService from '../services/VideoService';
import { useNavigate } from 'react-router-dom';

interface VideoGridProps {
    videos: Video[];
    onVideoClick?: (video: Video) => void;
}

const VideoGrid: React.FC<VideoGridProps> = ({ videos, onVideoClick }) => {
    const navigate = useNavigate();

    const handleVideoClick = (video: Video) => {
        if (onVideoClick) {
            onVideoClick(video);
        }
        navigate(`/video/${video.id}`);
    };

    const formatDuration = (seconds: number): string => {
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    };

    return (
        <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 row-cols-xl-4 g-4">
            {videos.map((video) => (
                <div key={video.id} className="col">
                    <div
                        className="card h-100"
                        style={{
                            cursor: 'pointer',
                            transition: 'transform 0.2s',
                            backgroundColor: 'var(--bg-secondary)',
                            border: '1px solid var(--border-color)',
                        }}
                        onClick={() => handleVideoClick(video)}
                        onMouseEnter={(e) => {
                            e.currentTarget.style.transform = 'translateY(-8px)';
                            e.currentTarget.style.borderColor = 'var(--accent-orange)';
                            e.currentTarget.style.boxShadow = '0 8px 16px rgba(255, 107, 53, 0.3)';
                        }}
                        onMouseLeave={(e) => {
                            e.currentTarget.style.transform = 'translateY(0)';
                            e.currentTarget.style.borderColor = 'var(--border-color)';
                            e.currentTarget.style.boxShadow = 'none';
                        }}
                    >
                        <img
                            src={videoService.getThumbnailUrl(video.id)}
                            className="card-img-top"
                            alt={video.title}
                            loading="lazy"
                            style={{
                                height: '200px',
                                objectFit: 'cover',
                                borderBottom: '1px solid var(--border-color)',
                            }}
                            onError={(e) => {
                                e.currentTarget.src = 'https://via.placeholder.com/320x180?text=No+Thumbnail';
                            }}
                        />
                        <div className="card-body" style={{ backgroundColor: 'var(--bg-secondary)' }}>
                            <h5
                                className="card-title text-truncate"
                                title={video.title}
                                style={{
                                    color: 'var(--text-primary)',
                                    overflow: 'hidden',
                                    textOverflow: 'ellipsis',
                                    display: '-webkit-box',
                                    WebkitLineClamp: 2,
                                    WebkitBoxOrient: 'vertical',
                                    fontSize: '0.95rem',
                                    lineHeight: '1.3',
                                }}
                            >
                                {video.title}
                            </h5>
                            <p className="card-text small mb-1" style={{ color: 'var(--text-secondary)' }}>
                                {video.user}
                            </p>
                            <p className="card-text small" style={{ color: 'var(--text-muted)' }}>
                                {video.viewCount} visualizaciones · {formatDuration(video.durationSeconds)}
                            </p>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default VideoGrid;
