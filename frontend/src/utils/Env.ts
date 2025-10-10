export const getEnv = () => {
  const {
    VITE_API_DOMAIN,
    VITE_MEDIA_DOMAIN,
    VITE_API_PORT,
    VITE_MEDIA_PORT,
    ...otherViteConfig
  } = import.meta.env as Record<string, string | undefined>;

  const protocol =
    typeof window !== 'undefined' ? window.location.protocol : 'http:';
  const hostname =
    typeof window !== 'undefined' ? window.location.hostname : 'localhost';

  const apiPort = VITE_API_PORT || '8080';
  const mediaPort = VITE_MEDIA_PORT || '8080';

  // If VITE_* is provided, use it. Otherwise, build from the current window host
  const apiDomain =
    (VITE_API_DOMAIN && VITE_API_DOMAIN.trim()) ||
    `${protocol}//${hostname}:${apiPort}`;

  const mediaDomain =
    (VITE_MEDIA_DOMAIN && VITE_MEDIA_DOMAIN.trim()) ||
    `${protocol}//${hostname}:${mediaPort}`;

  return {
    API_BASE_URL: `${apiDomain}/api`,
    MEDIA_BASE_URL: `${mediaDomain}/media`,
    __vite__: otherViteConfig,
  };
};
