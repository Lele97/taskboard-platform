
$authUrl = "http://localhost:8080/auth"
$projectUrl = "http://localhost:8080/api/projects"

# Register user
Write-Host "Registering user..."
$registerBody = @{
    username = "testuser"
    password = "password"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "$authUrl/register" -Method Post -Body $registerBody -ContentType "application/json"
    Write-Host "Registration response: $registerResponse"
} catch {
    Write-Host "Registration failed or user already exists."
}

# Login to get token
Write-Host "Logging in..."
$loginBody = @{
    username = "testuser"
    password = "password"
} | ConvertTo-Json

$loginResponse = Invoke-RestMethod -Uri "$authUrl/token" -Method Post -Body $loginBody -ContentType "application/json"
$token = $loginResponse.token
Write-Host "Token: $token"

$headers = @{
    Authorization = "Bearer $token"
}

# Create board
Write-Host "Creating board..."
$boardBody = @{
    name = "Test Board"
    description = "A test board"
} | ConvertTo-Json

$board = Invoke-RestMethod -Uri "$projectUrl/boards" -Method Post -Body $boardBody -ContentType "application/json" -Headers $headers
$boardId = $board.id
Write-Host "Created Board ID: $boardId"

# Get all boards
Write-Host "Getting all boards..."
$boards = Invoke-RestMethod -Uri "$projectUrl/boards" -Method Get -Headers $headers
Write-Host "Boards: " ($boards | ConvertTo-Json -Depth 3)

# Create card
Write-Host "Creating card..."
$cardBody = @{
    title = "Test Card"
    description = "A test card description"
    status = "TODO"
    boardId = $boardId
} | ConvertTo-Json

$card = Invoke-RestMethod -Uri "$projectUrl/cards" -Method Post -Body $cardBody -ContentType "application/json" -Headers $headers
$cardId = $card.id
Write-Host "Created Card ID: $cardId"

# Get cards for board
Write-Host "Getting cards for board $boardId..."
$cards = Invoke-RestMethod -Uri "$projectUrl/cards/board/$boardId" -Method Get -Headers $headers
Write-Host "Cards: " ($cards | ConvertTo-Json -Depth 3)

Write-Host "All tests finished."
