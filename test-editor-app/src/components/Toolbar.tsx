import CropSquareIcon from '@mui/icons-material/CropSquare';
import TitleIcon from '@mui/icons-material/Title';
import DeleteIcon from '@mui/icons-material/Delete';
import ReplayIcon from '@mui/icons-material/Replay';
import SaveIcon from '@mui/icons-material/Save';
import NoteAddIcon from '@mui/icons-material/NoteAdd';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import ImageIcon from "@mui/icons-material/Image";

interface ToolbarProps {
  setTool: (tool: string) => void;
}

export function Toolbar({ setTool }: ToolbarProps) {
  return (
    <div className="flex flex-col gap-5 w-20 justify-self-end">
      <button
        onClick={() => setTool("rectangle")}
        title="Прямоугольник"
        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center justify-center"
      >
        <CropSquareIcon fontSize="medium" />
      </button>
      <button
        onClick={() => setTool("image")}
        title="Вставить изображение"
        className="bg-yellow-400 text-white px-4 py-2 rounded hover:bg-yellow-500 flex items-center justify-center"
      >
        <ImageIcon fontSize="medium" />
      </button>
      <button
        onClick={() => setTool("text")}
        title="Текст"
        className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 flex items-center justify-center"
      >
        <TitleIcon fontSize="medium" />
      </button>
      <button
        onClick={() => setTool("update")}
        title="Сохранить"
        className="bg-purple-400 text-white px-4 py-2 rounded hover:bg-purple-600 flex items-center justify-center"
      >
        <SaveIcon fontSize="medium" />
      </button>
      <button
        onClick={() => setTool("save")}
        title="Сохранить как новую страницу"
        className="bg-purple-500 text-white px-4 py-2 rounded hover:bg-purple-600 flex items-center justify-center"
      >
        <ContentCopyIcon fontSize="medium" />
      </button>
      <button
        onClick={() => setTool("clear")}
        title="Очистить"
        className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 flex items-center justify-center"
      >
        <DeleteIcon fontSize="medium" />
      </button>
    </div>
  );
}
