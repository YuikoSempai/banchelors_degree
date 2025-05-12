import TestForm from "../components/TestForm";
import { useParams } from "react-router-dom";
import { Test } from "../types/test";
import { useEffect, useState } from "react";
// import * as fabric from 'fabric';
import { Quiz } from '../types/Quiz';

export default function CreateTest() {
  const [quizId, setQuizId] = useState<number>(Number(useParams().id));

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Создание теста</h1>
      <TestForm quizId={quizId!!}/>
    </div>
  );
}
