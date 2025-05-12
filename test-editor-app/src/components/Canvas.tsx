import { useEffect, useRef, useState } from "react";
import * as fabric from 'fabric';
import DivKitBuilder from './DivKitBuilder'
import { useParams } from "react-router-dom";
import { Toolbar } from "./Toolbar";
import { CustomBar } from "./CustomBar";
import { height, width } from "node_modules/@mui/system/esm/sizing/sizing";

interface CanvasProps {
    selectedTool: string;
    setTool: (tool: string) => void;
    page: number
}

export function Canvas({ selectedTool, setTool, page }: CanvasProps) {
    const INIT_TEXT = "Изменяемый текст!";

    const canvasRef = useRef<HTMLCanvasElement>(null);
    const quizId = Number(useParams().id!!)
    const fabricCanvas = useRef<fabric.Canvas | null>(null);
    const [selectedObject, setSelectedObject] = useState<fabric.Object | null>(null);
    const [canvasState, setCanvasState] = useState<string>();
    const [canvas, setCanvas] = useState<fabric.Canvas | null>(null);

    const sendQuiz = async (canvas: fabric.Canvas, data: any) => {
        let frontendDataToSend = JSON.stringify(canvas)
        let divKitDataToSet = JSON.stringify(data)
        try {
            const response = await fetch(`http://localhost:8080/quiz/${quizId}/page`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    divKitData: divKitDataToSet,
                    rawData: frontendDataToSend
                }),
            });

            if (!response.ok) {
                throw new Error(`Ошибка: ${response.status}`);
            }

            const result = await response.json();
            console.log('Ответ от сервера:', result);
        } catch (error) {
            console.error('Ошибка при отправке запроса:', error);
        }
    };

    const updateQuiz = async (canvas: fabric.Canvas, data: any) => {
        let frontendDataToSend = JSON.stringify(canvas)
        let divKitDataToSet = JSON.stringify(data)
        try {
            const response = await fetch(`http://localhost:8080/quiz/${quizId}/page`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    divKitData: divKitDataToSet,
                    rawData: frontendDataToSend,
                    id: page
                }),
            });

            if (!response.ok) {
                throw new Error(`Ошибка: ${response.status}`);
            }

            const result = await response.json();
            console.log('Ответ от сервера:', result);
        } catch (error) {
            console.error('Ошибка при отправке запроса:', error);
        }
    }

    const loadPageData = async (quizId: number) => {
        try {
            const response = await fetch(`http://localhost:8080/quiz/${quizId}/page/${page}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error(`Ошибка: ${response.status}`);
            }

            const result = await response.json();
            console.log('Ответ от сервера:', result);
            setCanvasState(result.rawData!!)
        } catch (error) {
            console.error('Ошибка при отправке запроса:', error);
        }
        return ""
    }

    useEffect(() => {
        let canvas = fabricCanvas.current
        console.log("[Canvas] Загрузка состояния с сервера")
        if (!canvas) {
            console.log("[Canvas] Can't load data. Canvas is null")
        }
        console.log("[Canvas] canvasState: " + canvasState + "\nCanvas: " + canvas)
        if (!canvas) {
            return
        }
        if (canvasState) {
            canvas.loadFromJSON(canvasState, function () {
                console.log("[Render canvas]" + canvas.item(0));
            });
        }
        console.log("[Canvas] Canvas: " + canvas)
        fabricCanvas.current = canvas;
        canvas.requestRenderAll();
        setCanvas(canvas)
    }, [canvasState])

    useEffect(() => {
        if (canvasRef.current) {
            let canvas = fabricCanvas.current;
            if (!canvas) {
                canvas = new fabric.Canvas(canvasRef.current, {
                    width: 350,
                    height: 650,
                    backgroundColor: "#ffffff",
                    selection: true,
                });
            }

            canvas.on('object:moving', (e) => {
                const obj = e.target;
                if (!obj) return;

                const padding = 0; // если хочешь отступ от границ

                const canvasWidth = canvas.getWidth();
                const canvasHeight = canvas.getHeight();
                // Проверяем, что объект является текстом
                if (obj.type === 'text' || obj.type === 'i-text') {
                    // Получаем реальные границы текста
                    const bounds = obj.getBoundingRect();

                    // Ограничиваем движение текста внутри поля canvas
                    const padding = 10;

                    // Логика для ограничения на оси X
                    if ((obj.left - (bounds.width / 2.0) - padding) < 0) {
                        obj.left = bounds.width / 2 + padding;
                    }
                    if ((obj.left + (bounds.width / 2.0) + padding) > canvasWidth) {
                        obj.left = canvasWidth - (bounds.width / 2) - padding;
                    }
                    if ((obj.top - (obj.height / 2.0) - padding) < 0) {
                        obj.top = (obj.height / 2.0) + padding
                    }
                    if ((obj.top + (obj.height / 2.0) + padding) > canvasHeight) {
                        obj.top = canvasHeight - (obj.height / 2.0) - padding
                    }
                } else {
                    // Левая граница
                    if (obj.left! < padding) {
                        obj.left = padding;
                    }

                    // Верхняя граница
                    if (obj.top! < padding) {
                        obj.top = padding;
                    }

                    // Правая граница
                    if (obj.left! + obj.width! * obj.scaleX! > canvas.width! - padding) {
                        obj.left = canvas.width! - obj.width! * obj.scaleX! - padding;
                    }

                    // Нижняя граница
                    if (obj.top! + obj.height! * obj.scaleY! > canvas.height! - padding) {
                        obj.top = canvas.height! - obj.height! * obj.scaleY! - padding;
                    }
                }
            });
            fabricCanvas.current = canvas;


            loadPageData(quizId)
        } else {
            console.log("Canvas ref is null")
        }
    }, [page]);

    useEffect(() => {
        const canvas = fabricCanvas.current;
        if (!canvas) return;

        const canvasWidth = canvas.getWidth();
        const canvasHeight = canvas.getHeight();

        if (selectedTool === "rectangle") {
            console.log("draw rectangle")
            const rectWidth = 120;
            const rectHeight = 80;

            const rect = new fabric.Rect({
                left: (canvasWidth - rectWidth) / 2, // по центру
                top: (canvasHeight - rectHeight) / 2, // по центру
                width: rectWidth,
                height: rectHeight,
                fill: "#0000FF",
                selectable: true,
                isCorrect: false,
                rx: 10,
                ry: 10
            });
            const textbox = new fabric.Textbox("Введите текст", {
                left: canvasWidth / 2,
                top: canvasHeight / 2,
                fontSize: 20,
                originX: 'center',
                originY: 'center',
                editable: true,
                textAlign: 'center'
            })

            const group = new fabric.Group([rect, textbox], {
                // height: rectHeight,
                // width: rectWidth,
                selectable: true,
                hasBorders: true,
                hasControls: true,
            });
            console.log(group)
            canvas.add(group);
        }

        if (selectedTool === "image") {
            fabric.Image.fromURL(
                'https://static.vecteezy.com/system/resources/previews/022/169/932/non_2x/3d-quiz-gold-title-for-contest-show-gold-font-for-trivia-typography-design-template-on-black-tile-background-typeface-in-light-glitter-frame-creative-retro-background-for-competition-vector.jpg',
                { crossOrigin: 'anonymous' }
            ).then(img => {
                const scale = Math.min(
                    300 / img.width!,
                    200 / img.height!,
                    1 // Не увеличивать масштаб, только уменьшать (уберите если хотите разрешить увеличение)
                  );
                  
                img.set({
                    left: (canvasWidth - 100) / 2, // по центру
                    top: (canvasHeight - 100) / 2, // по центру
                    scaleX: scale,
                    scaleY: scale,
                    // width: 300,
                    // height: 200
                });
                canvas.add(img);
            });
        }

        if (selectedTool === "text") {
            const text = new fabric.IText("Введите текст", {
                left: canvasWidth / 2,
                top: canvasHeight / 2,
                fontSize: 20,
                originX: "center", // чтобы текст был реально в центре
                originY: "center",
            });
            canvas.add(text);
        }
        if (selectedTool === "clear") {
            console.log("Change tool to clear")
            canvas.clear(); // Полностью очищает полотно
        }
        if (selectedTool === "save") {
            console.log("[Canvas] saving")
            const divKitResult = DivKitBuilder.createCanvasObjects(canvas.getObjects())
            console.log("[Canvas] divKit: " + JSON.stringify(divKitResult))
            console.log("[Canvas] canvasObjects: " + canvas.getObjects())
            sendQuiz(canvas, divKitResult)
        }
        if (selectedTool === "update") {
            console.log("[Canvas] update")
            const divKitResult = DivKitBuilder.createCanvasObjects(canvas.getObjects())
            updateQuiz(canvas, divKitResult)
        }
        setTool("")
        setCanvas(canvas)
    }, [selectedTool]);

    useEffect(() => {
        // const canvas = fabricCanvas.current;
        // if (!canvas) return;

        // const handleObjectClick = (e: fabric.IEvent<MouseEvent>) => {
        //     const target = e.target;
        //     if (!target) return;

        //     const isCorrect = (target as any).isCorrect;

        //     if (isCorrect === true) {
        //         target.set('fill', 'green');
        //     } else {
        //         target.set('fill', 'red');
        //     }

        //     canvas.requestRenderAll();
        // };

        // canvas.on('mouse:down', handleObjectClick);

        // return () => {
        //     canvas.off('mouse:down', handleObjectClick);
        // };
    }, []);

    useEffect(() => {
        const canvas = fabricCanvas.current;
        if (!canvas) return;

        const handleSelection = (e: fabric.IEvent<MouseEvent>) => {
            const selected = (e as any).selected; // Fabric добавляет массив выбранных объектов
            if (selected && selected.length > 0) {
                setSelectedObject(selected[0]);
            } else {
                setSelectedObject(null);
            }
        };

        const handleClearSelection = () => {
            setSelectedObject(null);
        };

        canvas.on('selection:created', handleSelection);
        canvas.on('selection:updated', handleSelection);
        canvas.on('selection:cleared', handleClearSelection);

        return () => {
            canvas.off('selection:created', handleSelection);
            canvas.off('selection:updated', handleSelection);
            canvas.off('selection:cleared', handleClearSelection);
        };
    }, []);

    const handleChangeCorrect = (isCorrect: boolean) => {
        if (!selectedObject) return;

        (selectedObject as any).isCorrect = isCorrect;

        // Обновляем цвет
        selectedObject.set('fill', isCorrect ? 'green' : 'red');

        fabricCanvas.current?.requestRenderAll();
    };

    return (
        <div className="w-full h-full bg-gray-100 py-6 flex justify-center items-start">
            <div className="flex items-start ">
                <div className="mt-[180px] mr-5 px-5" style={{ width: "325px", height: "256px" }}>
                    <Toolbar setTool={setTool} />
                </div>
            </div>
            <div className="flex justify-center items-start">
                <canvas
                    ref={canvasRef}
                    width={390}
                    height={600}
                    className="border-2 border-black rounded-lg shadow-lg"
                ></canvas>
            </div>
            <div className="flex items-start">
                <div className="mt-[195px] ml-5">
                    <CustomBar setSelectedItem={setSelectedObject} selectedItem={selectedObject} canvas={canvas} />
                </div>
            </div>
        </div>
    );
}
