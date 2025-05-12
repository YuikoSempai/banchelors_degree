import { useEffect, useState } from "react";
import {
  Box, Typography, Card, CardContent, Grid, Chip, Avatar, Stack, IconButton
} from "@mui/material";
import GroupIcon from "@mui/icons-material/Group";
import EditIcon from "@mui/icons-material/Edit";
import { Room } from "@/types/Common";

interface RoomListProps {
  onEdit: (room: Room) => void;
}

export default function RoomList() {
  const [rooms, setRooms] = useState<Room[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/room")
      .then(res => res.json())
      .then(data => setRooms(data.result ?? data))
      .catch(() => setRooms([]));
  }, []);

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" fontWeight={800} mb={4}>
        Список комнат
      </Typography>
      <Grid container spacing={3}>
        {rooms.map((room) => (
          <Grid size={{xs: 12, sm: 6, md: 4, lg: 3}} key={room.id}>
            <Card elevation={4} sx={{ p: 1, borderRadius: 3, position: 'relative' }}>
              <IconButton
                size="small"
                color="primary"
                sx={{ position: 'absolute', top: 8, right: 8, zIndex: 2 }}
                // onClick={() => onEdit(room)}
              >
                <EditIcon />
              </IconButton>
              <CardContent>
                <Stack direction="row" alignItems="center" spacing={1} mb={2}>
                  <GroupIcon sx={{ color: "#47528b" }} />
                  <Typography variant="h6" fontWeight={700}>
                    {room.name}
                  </Typography>
                </Stack>
                <Typography color="text.secondary" fontSize={15} fontWeight={500} mb={1}>
                  Пользователи:
                </Typography>
                <Stack direction="row" flexWrap="wrap" gap={1} mb={2}>
                  {room.users?.length
                    ? room.users.map((u) => (
                        <Chip key={u} label={u} size="small" />
                      ))
                    : <Typography color="text.disabled">Нет</Typography>}
                </Stack>
                <Typography color="text.secondary" fontSize={15} fontWeight={500} mb={1}>
                  Тесты:
                </Typography>
                <Stack direction="row" flexWrap="wrap" gap={1}>
                  {room.quizNames?.length
                    ? room.quizNames.map((q) => (
                        <Chip
                          key={q}
                          avatar={<Avatar sx={{ width: 20, height: 20, bgcolor: "#47528b" }}>Q</Avatar>}
                          label={q}
                          variant="outlined"
                          size="small"
                        />
                      ))
                    : <Typography color="text.disabled">Нет</Typography>}
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
