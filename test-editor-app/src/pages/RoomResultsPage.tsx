import { useEffect, useState } from "react";
import {
    Box,
    Typography,
    TableContainer,
    Table,
    TableHead,
    TableBody,
    TableCell,
    TableRow,
    Paper,
    Chip,
    Select,
    MenuItem,
    FormControl,
    InputLabel,
} from "@mui/material";
import { Room, UserResult } from "@/types/Common";


// Предполагается, что список комнат приходит из API/props
export default function RoomResultsPage() {
    const [selectedRoomId, setSelectedRoomId] = useState<number | null>();
    const [results, setResults] = useState<UserResult[]>([]);
    const [rooms, setRooms] = useState<Room[]>([]);

    useEffect(() => {
        fetch("http://localhost:8080/room")
            .then((res) => res.json())
            .then((data) => {
                console.log(data.result ?? data)
                setRooms(data.result ?? data)
            })
            .catch(() => setRooms([]));
    }, [])

    useEffect(() => {
        if (!selectedRoomId) return;
        // Замените на свой эндпоинт!
        fetch(`http://localhost:8080/room/${selectedRoomId}/result`)
            .then((res) => res.json())
            .then((data) => setResults(data.results))
            .catch(() => setResults([]));
    }, [selectedRoomId]);

    return (
        <Box sx={{ p: 4 }}>
            <Typography variant="h4" fontWeight={800} mb={3}>
            Результаты участников
            </Typography>
            <Box maxWidth={400} sx={{ mb: 3 }}>
                <FormControl fullWidth>
                    <InputLabel>Выберите комнату</InputLabel>
                    <Select
                        value={selectedRoomId || ""}
                        label="Выберите комнату"
                        onChange={(e) => setSelectedRoomId(Number(e.target.value))}
                    >
                        {rooms.map((r) => (
                            <MenuItem value={r.id} key={r.id}>
                                {r.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </Box>
            {results.length === 0 ? (
                <Typography color="text.disabled" mt={6}>
                    Пока нет результатов
                </Typography>
            ) : (
                <TableContainer component={Paper} sx={{ mt: 2 }}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Позиция</TableCell>
                                <TableCell>Пользователь</TableCell>
                                {results[0].quizResults.map((qiuz, idx) => (
                                    <TableCell key={idx}>Тест {idx + 1}</TableCell>
                                ))}
                                <TableCell>Сумма</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {results.map((user, idx) => (
                                <TableRow key={idx}>
                                    <TableCell>{idx + 1}</TableCell>
                                    <TableCell>{user.userName}</TableCell>
                                    {user.quizResults.map((res, idx) => (
                                        <TableCell>
                                            {res}%
                                        </TableCell>
                                    ))}
                                    <TableCell>{user.quizResults.reduce((accumulator, currentValue) => { return accumulator + currentValue }, 0)}</TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}
        </Box>
    );
}
