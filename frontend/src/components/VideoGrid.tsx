import { useState, useEffect } from 'react';
import { getEnv } from '../utils/Env';

const VideoGrid = () => {
  const [someData, setSomeData] = useState([]);

  useEffect(() => {
    fetch(getEnv().API_BASE_URL + '/someEndpoint')
      .then((res) => {
        return res.json();
      })
      .then((data) => {
        setSomeData(data);
      });
  }, []);

  return (
    <ul className="row g-4">
      {someData?.map((entity) => (
        <li>{entity}</li>
      ))}
    </ul>
  );
};

export default VideoGrid;
