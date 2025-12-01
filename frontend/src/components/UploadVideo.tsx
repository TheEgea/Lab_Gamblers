import React, { useState, FormEvent, ChangeEvent } from 'react';

interface UploadResponse {
    id: string;
    title: string;
}

const UploadVideo: React.FC = () => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [tags, setTags] = useState('');
    const [file, setFile] = useState<File | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
        const f = e.target.files?.[0] || null;
        setFile(f);
    };

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        setError(null);
        setSuccess(null);

        if (!file) {
            setError('Debes seleccionar un archivo de vídeo.');
            return;
        }

        try {
            setLoading(true);

            const token = localStorage.getItem('authToken');
            if (!token) {
                setError('Debes estar autenticado para subir vídeos.');
                setLoading(false);
                return;
            }

            const formData = new FormData();
            formData.append('file', file);
            formData.append('title', title);
            formData.append('description', description);
            formData.append('tags', tags);

            const res = await fetch('http://localhost:8080/videos/upload', {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${token}`,
                },
                body: formData,
            });

            if (!res.ok) {
                const text = await res.text();
                throw new Error(text || 'Error al subir el vídeo.');
            }

            const data: UploadResponse = await res.json();
            setSuccess(`Vídeo subido correctamente: "${data.title}"`);
            setTitle('');
            setDescription('');
            setTags('');
            setFile(null);
        } catch (err: any) {
            console.error('Error al subir el vídeo:', err);
            setError(err.message || 'Error al subir el vídeo.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container py-4">
            <h2 className="text-light mb-3">Subir vídeo</h2>
            <p className="text-muted" style={{ fontSize: '0.9rem' }}>
                Rellena la información del vídeo y selecciona el archivo multimedia que quieres subir.
            </p>

            <div
                className="card border-0 shadow-sm"
                style={{ backgroundColor: '#18181b', color: '#f5f5f5' }}
            >
                <div className="card-body">
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label className="form-label">Título</label>
                            <input
                                type="text"
                                className="form-control"
                                value={title}
                                onChange={(e) => setTitle(e.target.value)}
                                required
                            />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Descripción</label>
                            <textarea
                                className="form-control"
                                rows={4}
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                            />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Tags (separados por comas)</label>
                            <input
                                type="text"
                                className="form-control"
                                value={tags}
                                onChange={(e) => setTags(e.target.value)}
                                placeholder="ej: música, directo, gaming"
                            />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Archivo de vídeo</label>
                            <input
                                type="file"
                                className="form-control"
                                accept="video/*"
                                onChange={handleFileChange}
                                required
                            />
                        </div>

                        {error && (
                            <div className="alert alert-danger py-2" role="alert">
                                {error}
                            </div>
                        )}
                        {success && (
                            <div className="alert alert-success py-2" role="alert">
                                {success}
                            </div>
                        )}

                        <button
                            type="submit"
                            className="btn"
                            style={{
                                backgroundColor: '#ff6b35',
                                bordercolor: '#ff6b35',
                                color: 'white',
                                fontWeight: 'bold',
                            }}
                            disabled={loading}
                        >
                            {loading ? 'Subiendo...' : 'Subir vídeo'}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default UploadVideo;