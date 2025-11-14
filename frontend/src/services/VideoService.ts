// frontend/src/services/VideoService.ts
import axios, { AxiosInstance } from 'axios';

// Configuración base - SIN /api porque el backend no lo usa
const API_BASE_URL = 'http://localhost:8080';
const MEDIA_BASE_URL = 'http://localhost:8080';

const videoApi: AxiosInstance = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor para añadir el token de autenticación
videoApi.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Interceptor de respuestas para manejar errores
videoApi.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('authToken');
            window.location.href = '/';
        }
        return Promise.reject(error);
    }
);

export interface Comment {
    texto: string;
    usuario: string;
    timestamp: string;
    likes: number;
}

export interface Video {
    id: string;
    title: string;
    user: string;
    channel?: string;
    description?: string;
    viewCount: number;
    likeCount: number;
    durationSeconds: number;
    createdAt: string;
    width: number;
    height: number;
    comments?: Comment[];
}

export const videoService = {
    // Obtener todos los videos
    getAllVideos: async (): Promise<Video[]> => {
        const response = await videoApi.get<Video[]>('/videos/getAll');
        return response.data;
    },

    // Obtener video por ID
    getVideoById: async (id: string): Promise<Video> => {
        const response = await videoApi.get<Video>(`/videos/${id}`);
        return response.data;
    },

    // Obtener video aleatorio
    getRandomVideo: async (): Promise<Video> => {
        const response = await videoApi.get<Video>('/videos/random');
        return response.data;
    },

    // URL para el streaming del video
    getVideoStreamUrl: (id: string): string => {
        return `${MEDIA_BASE_URL}/media/video/${id}`;
    },

    // URL para el thumbnail
    getThumbnailUrl: (id: string): string => {
        return `${MEDIA_BASE_URL}/media/thumbnail/${id}`;
    },
};

export default videoService;
