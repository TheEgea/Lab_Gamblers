export interface SubscriptionResponse {
    id: number;
    userId: string;
    channelName: string;
    subscribedAt: string;
}

export interface SubscriptionRequest {
    channelName: string;
}

export interface UserProfileResponse {
    id: string;
    username: string;
    email: string;
    subscriptionCount: number;
    subscribedChannels: string[];
}

const API_URL = 'http://localhost:8080'

class SubscriptionServiceClass {
    private getAuthToken(): string | null {
        return localStorage.getItem('authToken');
    }

    private getHeaders(): HeadersInit {
        const token = this.getAuthToken();
        const headers: HeadersInit = {
            'Content-Type': 'application/json',
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        return headers;
    }

    async subscribe(channelName: string): Promise<SubscriptionResponse> {
        const response = await fetch(`${API_URL}/subscriptions`, {
            method: 'POST',
            headers: this.getHeaders(),
            body: JSON.stringify({ channelName }),
        });

        if (!response.ok) {
            if (response.status === 400) {
                const error = await response.text();
                throw new Error(error || 'Ya estás suscrito a este canal.');
            }
            throw new Error('Error al suscribirse al canal.');
        }

        return response.json();
    }

    async unsubscribe(channelName: string): Promise<void> {
        const response = await fetch(`${API_URL}/api/subscriptions/${encodeURIComponent(channelName)}`, {
            method: 'DELETE',
            headers: this.getHeaders(),
        });

        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('No estás suscrito a este canal.');
            }
            throw new Error ('Error al cancelar la suscripción al canal.');
        }
    }

    async getUserSubscriptions(): Promise<SubscriptionResponse[]> {
        const response = await fetch(`${API_URL}/api/subscriptions`, {
            method: 'GET',
            headers: this.getHeaders(),
        });

        if (!response.ok) {
            throw new Error('Error al obtener las suscripciones del usuario.');
        }

        return response.json();
    }

    async isSubscribed(channelName: string): Promise<boolean> {
        const response = await fetch(
            `${API_URL}/api/subscriptions/check/${encodeURIComponent(channelName)}`, {
                method: 'GET',
                headers: this.getHeaders(),
            }
        );

        if (!response.ok) {
            throw new Error('Error al verificar la suscripción al canal.');
        }

        return response.json();
    }

    async getUserProfile(): Promise<UserProfileResponse> {
        const response = await fetch(`${API_URL}/users/profile`, {
            method: 'GET',
            headers: this.getHeaders(),
        });

        if (!response.ok) {
            if (response.status === 401) {
                throw new Error('No autorizado. Por favor, inicia sesión de nuevo.');
            }
            throw new Error('Error al obtener el perfil del usuario.');
        }

        return response.json();
    }

    async toggleSubscription(channelName: string): Promise<boolean> {
        const isCurrentlySubscribed = await this.isSubscribed(channelName);

        if (isCurrentlySubscribed) {
            await this.unsubscribe(channelName);
            return false; // Ahora está desuscrito
        } else {
            await this.subscribe(channelName);
            return true; // Ahora está suscrito
        }
    }
}

export const SubscriptionService = new SubscriptionServiceClass();

export default SubscriptionService;