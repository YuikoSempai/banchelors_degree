import { useEffect, useState } from "react";
import axios from "axios";
import { Box, Grid, Card, CardContent, Typography, Button, CardActions, Avatar, Fade } from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import QuizIcon from "@mui/icons-material/Quiz";
import SentimentSatisfiedAltIcon from "@mui/icons-material/SentimentSatisfiedAlt";
import { Quiz } from "@/types/Quiz";
import { QuizResponse } from "@/types/Common";
import { useNavigate } from "react-router-dom";

interface AllTestPageProps {
  onEditQuiz?: (quiz: Quiz) => void;
}

export default function QuizList({ onEditQuiz }: AllTestPageProps) {
  const [quizzes, setQuizzes] = useState<Quiz[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate()
  
  useEffect(() => {
    axios
      .get<QuizResponse>("http://localhost:8080/quiz")
      .then((res) => setQuizzes(res.data.result))
      .catch(() => setQuizzes([]))
      .finally(() => setLoading(false));
  }, []);

  return (
    <Box>
      <Typography variant="h3" fontWeight={900} textAlign="center" mb={4}>
        Список тестов
      </Typography>
      <Fade in={!loading}>
        <Box>
          {quizzes.length === 0 ? (
            <Box 
              sx={{
                display: "flex", 
                flexDirection: "column",
                alignItems: "center",
                mt: 8,
                opacity: 0.6,
              }}
            >
              <SentimentSatisfiedAltIcon color="disabled" sx={{ fontSize: 64 }} />
              <Typography variant="h6" mt={2}>Пока нет ни одного теста</Typography>
            </Box>
          ) : (
            <Grid container spacing={{ xs: 2, md: 10 }} sx={{justifyContent: "center" }}>
              {quizzes.map((quiz) => (
                <Grid sx={{width: '30vw', height: "20vw", xs: "12", display: "flex", flexDirection: "column", justifyContent: "space-between"}} key={quiz.id}>
                  <Card
                    elevation={5}
                    sx={{
                      height: "100%",
                      borderRadius: 4,
                      transition: "transform 0.15s, box-shadow 0.15s",
                      "&:hover": {
                        transform: "translateY(-7px) scale(1.025)",
                        boxShadow: 12,
                      },
                      display: "flex",
                      flexDirection: "column",
                      justifyContent: "space-between",
                      p: 2,
                      bgcolor: "background.paper",
                    }}
                  >
                    <CardContent>
                      <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                        <Avatar sx={{ bgcolor: "primary.main", mr: 2 }}>
                          <QuizIcon />
                        </Avatar>
                        <Typography variant="h6" component="div" fontWeight={700}>{quiz.name}</Typography>
                      </Box>
                      <Typography color="text.secondary" fontSize={15} fontWeight={500}>
                        Количество страниц: {quiz.pageCount}
                      </Typography>
                    </CardContent>
                    <CardActions>
                      <Button
                        size="small"
                        variant="contained"
                        color="primary"
                        startIcon={<EditIcon />}
                        fullWidth
                        onClick={() => navigate(`/create/${quiz.id}`)}
                        sx={{ borderRadius: 2, fontWeight: 600, letterSpacing: 1 }}
                      >
                        Редактировать
                      </Button>
                    </CardActions>
                  </Card>
                </Grid>
              ))}
            </Grid>
          )}
        </Box>
      </Fade>
    </Box>
  );
}
