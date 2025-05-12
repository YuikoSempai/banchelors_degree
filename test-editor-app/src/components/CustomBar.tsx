import * as fabric from 'fabric';
import { useEffect, useState } from 'react';

interface CustomBarProps {
  setSelectedItem: (tool: fabric.Object | null) => void
  selectedItem: fabric.Object | null
  canvas: fabric.Canvas | null
}

export function CustomBar({ setSelectedItem, selectedItem, canvas }: CustomBarProps) {

  const [buttonText, setButtonText] = useState<string>()
  const [buttonColor, setButtonColor] = useState<string>()
  const [isCorrect, setIsCorrect] = useState<boolean>()
  useEffect(() => {
    if (selectedItem == null) {
      return
    }
    if (selectedItem.type == "group") {
      let textBox = ((selectedItem as fabric.Group).item(1) as fabric.Textbox);
      let rect = ((selectedItem as fabric.Group).item(0) as fabric.Rect);
      setButtonText(textBox.text)
      setButtonColor((rect.fill as string))
      setIsCorrect((rect.isCorrect as boolean))
    }
  }, [selectedItem])

  function changeButtonText(group: fabric.Object, newText: string) {
    // 0 is rectangle | 1 is text box
    if (group.type == "group") {
      ((group as fabric.Group).item(1)).set({ text: newText })
      setButtonText(newText);
      canvas!!.requestRenderAll();
    }
  }

  function changeButtonColor(object: fabric.Object, newColor: string) {
    if (object.type == "group") {
      ((object as fabric.Group).item(0)).set({ fill: newColor })
      setButtonColor(newColor)
      canvas!!.requestRenderAll();
    }
  }

  function changeIsCorrect(object: fabric.Object, isCorrect: boolean) {
    if (object.type == "group") {
      ((object as fabric.Group).item(0)).set({ isCorrect: isCorrect })
      setIsCorrect(isCorrect)
      canvas!!.requestRenderAll();
    }
  }

  function deleteButton(object: fabric.Object) {
    canvas?.remove(object)
    setSelectedItem(null)
  }

  function copy() {
    canvas?.add(selectedItem!!)
    canvas!!.requestRenderAll()
  }


  return (
    <div className="flex flex-col top-10 gap-2 bg-white p-3 rounded-xl shadow-lg flex flex-col" style={{ width: "325px" }}>
      {canvas != null && selectedItem?.type === "group" ? (
        <>
          <div className="font-bold text-lg mb-4">Настройки прямоугольника</div>
          <label className="block mb-2 font-medium">Текст внутри:</label>
          <input
            type="text"
            className="w-full px-3 py-2 mb-4 border rounded"
            value={buttonText}
            onChange={e => changeButtonText(selectedItem, e.target.value)}
          />
          <label className="block mb-2 font-medium">Цвет:</label>
          <input
            type="color"
            className="w-12 h-10 mb-2 p-0 border-none"
            value={buttonColor}
            onChange={e => changeButtonColor(selectedItem, e.target.value)}
          />
          {/* Переключатель правильно/неправильно */}
          <span className="font-medium mr-2">
          Правильный ответ: 
          <input
            type="checkbox"
            id="rect-is-correct"
            checked={!!isCorrect}
            onChange={e => changeIsCorrect(selectedItem, e.target.checked)}
            className="align-middle"
          />
          </span>
          {/* <label htmlFor="rect-is-correct" className="ml-2 select-none">
              {selectedItem.isCorrect ? "Да" : "Нет"}
            </label> */}
          {/* <button
            onClick={e => deleteButton(selectedItem)}
          >
            delete
          </button>
          <button
            onClick={e => copy()}
          >
            copy
          </button> */}
        </>
      ) : (
        <div className="flex items-center justify-center h-full text-gray-400 font-medium">
          Выберите объект для редактирования
        </div>
      )
      }
    </div >
  );
}
