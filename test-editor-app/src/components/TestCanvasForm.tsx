import { useEffect, useState } from "react";
import { Canvas } from "./Canvas";
import { Toolbar } from "./Toolbar";
import Draggable, { DraggableCore } from 'react-draggable';
import * as fabric from 'fabric';
import { Quiz } from "../types/Quiz";

interface TestCanvasFormProps {
    quizId: number,
}

export function TestCanvasForm({ quizId }: TestCanvasFormProps) {
    const [selectedTool, setSelectedTool] = useState("");
    const [page, setPage] = useState(1);
    const [quiz, setQuiz] = useState<Quiz>()
    const [pageCount, setPageCount] = useState<number>(1);
    useEffect(() => {
        requestQuiz()
    }, [])

    function requestQuiz() {
        fetch(`http://localhost:8080/quiz/${quizId}`)
            .then((res) => res.json())
            .then((data) => {
                setQuiz(data)
                setPageCount(data.pageCount)
                console.log("[CreateTest] Saved quiz state: " + data.pageCount)
            });
    }

    function updateQuiz() {
        fetch(`http://localhost:8080/quiz/${quizId}`)
            .then((res) => res.json())
            .then((data) => {
                setQuiz(data)
                setPageCount(data.pageCount + 1)
                setPage(data.pageCount + 1)
                console.log("[CreateTest] Saved quiz state: " + data.pageCount)
            });
    }

    useEffect(() => {
        requestQuiz()
    }, [])

    useEffect(() => {
        console.log("[TestCanvasForm] selectedTool: " + selectedTool)
        if (selectedTool === "save") {
            updateQuiz()
        }
    }, [selectedTool]);

    const prevPage = () => setPage(p => (p > 1 ? p - 1 : 1));
    const nextPage = () => setPage(p => (p < pageCount ? p + 1 : p));

    return (
        <div
            id="canvas-wrapper"
            className="relative overflow-hidden"
        >
            <div style={{ display: "flex", alignItems: "center", justifyContent: "center", marginBottom: 12, marginRight: 20 }}>
                <button
                    onClick={prevPage}
                    disabled={page === 1}
                    style={{ marginRight: 12, padding: "6px 16px" }}
                >
                    Назад
                </button>
                <span style={{ fontWeight: 500 }}>Страница {page} из {pageCount}</span>
                <button
                    onClick={nextPage}
                    disabled={page == pageCount}
                    style={{ marginLeft: 12, padding: "6px 16px" }}
                >
                    Вперёд
                </button>
            </div>
            <div>
                <Canvas selectedTool={selectedTool} setTool={setSelectedTool} page={page} />
            </div>
        </div>
    );
}
