// frontend/src/components/Home.tsx
import { useEffect, useState } from 'react';
import VideoGrid from './VideoGrid';
import videoService, { Video } from '../services/VideoService';

const Home = () => {
    const [videos, setVideos] = useState<Video[]>([]);
    const [status, setStatus] = useState<'idle' | 'loading' | 'error' | 'success'>('idle');
    const [message, setMessage] = useState<string>('');

    const fetchVideos = async () => {
        try {
            setStatus('loading');
            setMessage('');

            const data = await videoService.getAllVideos();
            setVideos(data);
            setStatus('success');
        } catch (e) {
            const errorMsg = e instanceof Error ? e.message : 'Error desconocido';
            setMessage(errorMsg);
            setStatus('error');
            console.error('Error fetching videos:', e);
        }
    };

    useEffect(() => {
        fetchVideos();
    }, []);

    if (status === 'loading') {
        return (
            <div style={{ padding: 24, textAlign: 'center' }}>
                <div className="spinner-border" role="status">
                    <span className="visually-hidden">Cargando...</span>
                </div>
                <p className="mt-2">Cargando videos…</p>
            </div>
        );
    }

    if (status === 'error') {
        return (
            <div style={{ padding: 24 }}>
                <div className="alert alert-danger" role="alert">
                    <h4 className="alert-heading">Error</h4>
                    <p>{message}</p>
                    <button onClick={fetchVideos} className="btn btn-primary">
                        Reintentar
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div
            className="container-fluid"
            style={{
                padding: 24,
                backgroundColor: 'var(--bg-primary)',
                minHeight: '100vh',
            }}
        >
            <h2 className="mb-4" style={{ color: 'var(--text-primary)' }}>
                Vídeos Disponibles
            </h2>
            {videos.length === 0 ? (
                <div
                    className="alert"
                    style={{
                        backgroundColor: 'var(--bg-secondary)',
                        border: '1px solid var(--accent-orange)',
                        color: 'var(--text-primary)',
                    }}
                >
                    No hay videos disponibles
                </div>
            ) : (
                <VideoGrid videos={videos} onVideoClick={(v) => console.log('Video clicked:', v)} />
            )}
        </div>
    );
};

export default Home;
