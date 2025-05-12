import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Главная</h1>
      <p>Добро пожаловать! Используй меню слева для создания или редактирования тестов.</p>
      <Link to="/create" className="text-blue-600 underline">
        ➕ Создать новый тест
      </Link>
    </div>
  );
}
