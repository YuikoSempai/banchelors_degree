import { useParams } from "react-router-dom";
import TestForm from "../components/TestForm";
import { Test } from "../types/test";
import { useEffect, useState } from "react";

export default function EditTest() {
  const { id } = useParams(); // если id есть, значит мы в режиме редактирования
  
  // const
  const handleSubmit = (data: Test) => {
    console.log("Создан тест:", data);
    alert("Тест сохранён.");
  };

  useEffect(() => {
    console.log("Edit test")
    if (id) {
      // Загрузить тест по id с сервера
      fetch(`http://localhost:8080/quiz/${id}`)
        .then((res) => res.json())
        .then((data) => {
          // Установить состояние формы (setTest)
          console.log(data)
        });
    }
  }, [id]);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Редактирование теста</h1>
      {/* <TestForm  onSubmit={handleSubmit} /> */}
    </div>
  );
}
