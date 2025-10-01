
#!/bin/bash
# deploy.sh - Script para actualizar Pro Tube
echo "🚀 Iniciando deploy de Pro Tube..."
echo "======================================"

# Navegar al proyecto correcto
cd /home/lab/protube/Lab_Gamblers

# 1. Actualizar código desde Git
echo "📥 Actualizando código desde Git..."
git pull origin main

# 2. Backend
echo "🔨 Compilando backend..."
cd backend
mvn clean package -P prod

# 3. Frontend  
echo "🎨 Compilando frontend..."
cd ../frontend
npm install
npm run build

# 4. Volver al directorio del proyecto
cd ..

echo "✅ Deploy completado!"
echo "📋 Para ejecutar en producción:"
echo "   cd backend && java -jar target/*.jar"
echo ""
echo "📋 Para desarrollo:"
echo "   Backend: cd backend && mvn spring-boot:run -P dev"
echo "   Frontend: cd frontend && npm run dev"
