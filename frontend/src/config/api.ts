// Esta es la URL base de tu backend
export const API_BASE_URL = 'http://localhost:8080';

// Funciones helper para construir URLs
export const buildApiUrl = (path: string): string => {
    // Asegurar que path empiece con /
    const cleanPath = path.startsWith('/') ? path : `/${path}`;
    return `${API_BASE_URL}${cleanPath}`;
};
