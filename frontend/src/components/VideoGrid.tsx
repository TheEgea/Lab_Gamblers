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
                        className="card h-100 shadow-sm"
                        style={{ cursor: 'pointer', transition: 'transform 0.2s' }}
                        onClick={() => handleVideoClick(video)}
                        onMouseEnter={(e) => {
                            e.currentTarget.style.transform = 'translateY(-5px)';
                        }}
                        onMouseLeave={(e) => {
                            e.currentTarget.style.transform = 'translateY(0)';
                        }}
                    >
                        <img
                            src={videoService.getThumbnailUrl(video.id)}
                            className="card-img-top"
                            alt={video.title}
                            style={{ height: '200px', objectFit: 'cover' }}
                            onError={(e) => {
                                e.currentTarget.src = 'https://via.placeholder.com/320x180?text=No+Thumbnail';
                            }}
                        />
                        <div className="card-body">
                            <h5 className="card-title text-truncate" title={video.title}>
                                {video.title}
                            </h5>
                            <p className="card-text text-muted small mb-1">{video.user}</p>
                            <p className="card-text text-muted small">
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
