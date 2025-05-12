import { useState, useEffect, FormEvent } from "react";
import { Test } from "../types/test";
import { TestCanvasForm } from "./TestCanvasForm"
import { Quiz } from "../types/Quiz";


interface TestFormProps {
    quizId: number
}

export default function TestForm({ quizId }: TestFormProps) {
    return (
        <div>
            <div>
                <TestCanvasForm quizId={quizId} />
            </div>
        </div>
    );
}
