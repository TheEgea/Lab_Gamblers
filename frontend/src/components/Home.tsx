import { useEffect, useState } from 'react';
import VideoGrid from './VideoGrid';

type Video = {
    id: number;
    title: string;
    duration: number;
    width: number;
    height: number;
    user: string;
    description?: string;
};

const Home = () => {
    const [videos, setVideos] = useState<Video[]>([]);
    const [status, setStatus] = useState<'idle'|'loading'|'error'|'success'>('idle');
    const [message, setMessage] = useState<string>('');

    const fetchVideos = async () => {
        try {
            setStatus('loading');
            setMessage('');
            const token = localStorage.getItem('authToken');
            const res = await fetch('/api/videos', {
                // Se usa ruta relativa para respetar tu autodetección de host/puerto
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) throw new Error(`Error ${res.status} al cargar videos`);
            const data = await res.json();
            setVideos(Array.isArray(data) ? data : []);
            setStatus('success');
        } catch (e) {
            setMessage(e instanceof Error ? e.message : 'Error desconocido');
            setStatus('error');
        }
    };

    useEffect(() => { fetchVideos(); }, []);

    if (status === 'loading') return <div style={{ padding: 24 }}>Cargando videos…</div>;
    if (status === 'error') return (
        <div style={{ padding: 24 }}>
            <h3>Error</h3>
            <p>{message}</p>
            <button onClick={fetchVideos}>Reintentar</button>
        </div>
    );

    return (
        <div style={{ padding: 24 }}>
            <h2>Videos</h2>
            {videos.length === 0
                ? <div>No hay videos disponibles</div>
                : <VideoGrid videos={videos} onVideoClick={(v) => console.log('clicked', v)} />
            }
        </div>
    );
};

export default Home;
