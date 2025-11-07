// frontend/src/services/AuthService.ts
import axios, { AxiosInstance, AxiosError } from 'axios';
import { LoginRequest, LoginResponse, User } from '../types/auth.types';

const API_URL = 'http://localhost:8080';

const api: AxiosInstance = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor para añadir token
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken'); // ← CAMBIADO
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Interceptor de respuestas
api.interceptors.response.use(
    (response) => response,
    (error: AxiosError) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('authToken'); // ← CAMBIADO
            localStorage.removeItem('user');
            window.location.href = '/';
        }
        return Promise.reject(error);
    }
);

export const authService = {
    login: async (username: string, password: string): Promise<LoginResponse> => {
        try {
            const response = await api.post<LoginResponse>('/auth/login', {
                username,
                password,
            } as LoginRequest);

            if (response.data.token) {
                localStorage.setItem('authToken', response.data.token); // ← CAMBIADO
                localStorage.setItem('user', JSON.stringify(response.data.user));
            }

            return response.data;
        } catch (error) {
            if (axios.isAxiosError(error)) {
                throw new Error(error.response?.data?.message || 'Error al iniciar sesión');
            }
            throw error;
        }
    },

    logout: (): void => {
        localStorage.removeItem('authToken'); // ← CAMBIADO
        localStorage.removeItem('user');
    },

    signup: async (username: string, password: string, email: string): Promise<LoginResponse> => {
        try {
            const response = await api.post<LoginResponse>('/auth/register', {
                username,
                password,
                email,
            });

            if (response.data.token) {
                localStorage.setItem('authToken', response.data.token); // ← CAMBIADO
                localStorage.setItem('user', JSON.stringify(response.data.user));
            }

            return response.data;
        } catch (error) {
            if (axios.isAxiosError(error)) {
                throw new Error(error.response?.data?.message || 'Error al registrarse');
            }
            throw error;
        }
    },

    getCurrentUser: async (): Promise<User> => {
        try {
            const response = await api.get<User>('/auth/me');
            return response.data;
        } catch (error) {
            if (axios.isAxiosError(error)) {
                throw new Error(error.response?.data?.message || 'Error al obtener usuario');
            }
            throw error;
        }
    },

    isAuthenticated: (): boolean => {
        return !!localStorage.getItem('authToken'); // ← CAMBIADO
    },

    getStoredUser: (): User | null => {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    },
};

export default authService;
