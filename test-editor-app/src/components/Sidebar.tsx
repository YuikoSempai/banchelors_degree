

// export default function Sidebar() {
//   return (
//     <aside className="w-64 min-h-screen bg-gray-100 p-4">
//       <h2 className="text-xl font-bold mb-6">📋 Меню</h2>
//       <nav className="flex flex-col space-y-4">
//         <NavLink to="/" className={({ isActive }) => isActive ? "font-bold text-blue-600" : "text-gray-800"}>
//           🏠 Главная
//         </NavLink>
//         {/* <NavLink to="/create" className={({ isActive }) => isActive ? "font-bold text-blue-600" : "text-gray-800"}>
//           ➕ Новый тест
//         </NavLink> */}
//         <NavLink to="/tests" className={({ isActive }) => isActive ? "font-bold text-blue-600" : "text-gray-800"}>
//           Список всех тестов
//         </NavLink>
//       </nav>
//     </aside>
//   );
// }
import { NavLink } from "react-router-dom";
import { Box, Typography, Divider, List, ListItem, ListItemButton, ListItemIcon, ListItemText } from "@mui/material";
import QuizIcon from "@mui/icons-material/Quiz";
import EditIcon from "@mui/icons-material/Edit";
import GroupIcon from "@mui/icons-material/Groups";
import { useNavigate } from 'react-router-dom';

export default function Sidebar() {

  const navigate = useNavigate();

  return (
    <Box
        sx={{
            width: 240,
            height: "100vh",
            bgcolor: "background.paper",
            display: "flex",
            flexDirection: "column",
            borderRight: "1px solid #e0e0e0"
        }}
    >
        <Box sx={{ px: 3, py: 4 }}>
            <Typography
                variant="h5"
                fontWeight={900}
                sx={{
                    letterSpacing: -1,
                    color: "#232323",
                    fontFamily: '"Inter","Roboto","Arial",sans-serif',
                }}
            >
                UTesting
            </Typography>
        </Box>
        <Divider />

        {/* Блок "Тесты" */}
        <Box sx={{ px: 2, pt: 2, pb: 0.5 }}>
            <Typography variant="caption" sx={{ color: "#666", fontWeight: 700, letterSpacing: 1 }}>
                ЗАДАНИЯ
            </Typography>
        </Box>
        <List>
            <ListItem disablePadding>
                <ListItemButton
                    onClick={() => navigate("/tests")}
                >
                    <ListItemIcon><QuizIcon /></ListItemIcon>
                    <ListItemText primary="Список заданий" />
                </ListItemButton>
            </ListItem>
            <ListItem disablePadding>
                <ListItemButton
                    onClick={() => navigate("/create/1")}
                >
                    <ListItemIcon><EditIcon /></ListItemIcon>
                    <ListItemText primary="Редактор" />
                </ListItemButton>
            </ListItem>
        </List>

        {/* Разделитель раздела */}
        <Divider sx={{ my: 2 }} />

        {/* Блок "Комнаты" */}
        <Box sx={{ px: 2, pt: 0.5, pb: 0.5 }}>
            <Typography variant="caption" sx={{ color: "#666", fontWeight: 700, letterSpacing: 1 }}>
                СОРЕВНОВАНИЯ
            </Typography>
        </Box>
        <List>
            <ListItem disablePadding>
                <ListItemButton
                    onClick={() => navigate("/rooms")}
                >
                    <ListItemIcon><GroupIcon /></ListItemIcon>
                    <ListItemText primary="Комнаты" />
                </ListItemButton>
            </ListItem>
        </List>
        <List>
            <ListItem disablePadding>
                <ListItemButton
                onClick={() => navigate("/rooms/result")}
                >
                    <ListItemIcon><GroupIcon /></ListItemIcon>
                    <ListItemText primary="Результаты" />
                </ListItemButton>
            </ListItem>
        </List>

        {/* Если нужно добавить другие страницы — добавляйте аналогично! */}
    </Box>
);
}
