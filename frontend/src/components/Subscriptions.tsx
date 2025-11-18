import React, { useEffect, useState } from 'react';
import { SubscriptionService, SubscriptionResponse } from '../services/SubscriptionService';
import { VideoService } from '../services/VideoService';
import VideoGrid from './VideoGrid';

interface Video {
    id: string;
    title: string;
    description: string;
    thumbnailUrl: string;
    uploaderUsername: string;
    uploadDate: string;
}

const Subscriptions: React.FC = () => {
    const [subscriptions, setSubscriptions] = useState<SubscriptionResponse[]>([]);
    const [videos, setVideos] = useState<Video[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        loadSubscriptionsAndVideos();
    }, []);

    const loadSubscriptionsAndVideos = async () => {
        try {
            setLoading(true);
            setError(null);

            const subs = await SubscriptionService.getUserSubscriptions();
            setSubscriptions(subs);

            const allVideos = await VideoService.getAllVideos();

            const subscribedChannels = subs.map(s => s.channelName);
            const filteredVideos = allVideos.filter(video => subscribedChannels.includes(video.uploaderUsername));

            filteredVideos.sort((a, b) =>
                new Date(b.uploadDate).getTime() - new Date(a.uploadDate).getTime()
            );

            setVideos(filteredVideos);
        } catch (error) {
            console.error('Error loading subscriptions:', error);
            setError('Error al cargar las suscripciones');
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="container mt-5">
                <div className="alert alert-danger" role="alert">
                    {error}
                </div>
            </div>
        );
    }

    return (
        <div className="container mt-4">
            <h2 className="mb-4">Suscripciones</h2>

            {subscriptions.length === 0 ? (
                <div className="alert alert-info" role="alert">
                    No estás suscrito a ningún canal todavía. ¡Explora y suscríbete a tus canales favoritos!
                </div>
            ) : (
                <>
                    <p className="text-muted mb-4">
                        Estás suscrito a {subscriptions.length} {subscriptions.length === 1 ? 'canal' : 'canales'}
                    </p>

                    {videos.length === 0 ? (
                        <div className="alert alert-info" role="alert">
                            Los canales a los que estás suscrito aún no han subido vídeos.
                        </div>
                    ) : (
                        <VideoGrid videos={videos} />
                    )}
                </>
            )}
        </div>
    );
};

export default Subscriptions;